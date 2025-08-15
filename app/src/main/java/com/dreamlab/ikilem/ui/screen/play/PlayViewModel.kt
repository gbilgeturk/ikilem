package com.dreamlab.ikilem.ui.screen.play

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dreamlab.ikilem.data.model.Category
import com.dreamlab.ikilem.data.model.Dilemma
import com.dreamlab.ikilem.data.repo.DilemmaRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.random.Random

class PlayViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = DilemmaRepository(app.applicationContext)
    private val db = FirebaseFirestore.getInstance()

    private val _current = MutableStateFlow<Dilemma?>(null)
    val current: StateFlow<Dilemma?> = _current

    private val _votes = MutableStateFlow(0 to 0)
    val votes: StateFlow<Pair<Int, Int>> = _votes

    private val _hasChosen = MutableStateFlow(false)
    val hasChosen: StateFlow<Boolean> = _hasChosen

    // kullanıcı oy tuşuna basıp network round-trip beklerken true olur
    private val _isVoting = MutableStateFlow(false)

    private var listJob: Job? = null
    private var docListener: com.google.firebase.firestore.ListenerRegistration? = null
    private var latestList: List<Dilemma> = emptyList()

    // sadece kullanıcı "Sonraki ikilem" dediğinde yeni seçim yap
    private var allowPickNew = false

    fun load(category: Category? = null) {
        // ilk ekranda tek soru seçilsin
        allowPickNew = true
        _hasChosen.value = false
        _isVoting.value = false
        _votes.value = 0 to 0
        startListStream(category)
    }

    private fun startListStream(category: Category?) {
        listJob?.cancel()
        listJob = viewModelScope.launch {
            // liste içerik olarak aynı kaldıkça tetiklenmesin diye sadece ID setine göre değişimi izle
            repo.streamDilemmas(category)
                .distinctUntilChanged { old, new ->
                    old.map { it.id }.toSet() == new.map { it.id }.toSet()
                }
                .collect { list ->
                    latestList = list

                    // hiç soru yoksa dokunma (mevcut soruyu koru)
                    if (list.isEmpty()) return@collect

                    val cur = _current.value
                    val stillExists = cur != null && list.any { it.id == cur.id }

                    // yalnızca 3 durumda yeni soru seç:
                    // 1) current null (ilk geliş)
                    // 2) mevcut soru listeden silinmiş
                    // 3) kullanıcı "Sonraki ikilem" demiş (allowPickNew = true)
                    if (cur == null || !stillExists || allowPickNew) {
                        pickAndSetNew(list, excludeId = cur?.id)
                        allowPickNew = false
                    } else {
                        // aynı soruda yüzdeler güncellenmeye devam eder
                        // (docListener zaten bağlı)
                    }
                }
        }
    }

    private fun pickAndSetNew(list: List<Dilemma>, excludeId: String?) {
        val candidates = if (excludeId != null) list.filter { it.id != excludeId } else list
        val pick = (if (candidates.isNotEmpty()) candidates else list)[Random.nextInt((if (candidates.isNotEmpty()) candidates else list).size)]
        setCurrent(pick)
        _hasChosen.value = false
        _isVoting.value = false
        _votes.value = 0 to 0
    }

    private fun setCurrent(d: Dilemma?) {
        _current.value = d
        docListener?.remove()
        if (d == null) return
        docListener = db.collection("dilemmas").document(d.id)
            .addSnapshotListener { snap, _ ->
                val a = snap?.getLong("votesA")?.toInt() ?: 0
                val b = snap?.getLong("votesB")?.toInt() ?: 0
                _votes.value = a to b
            }
    }

    fun next(category: Category?) {
        // kullanıcı istedi: sonraki soru seçilsin
        allowPickNew = true
        if (latestList.isNotEmpty()) {
            pickAndSetNew(latestList, excludeId = _current.value?.id)
            allowPickNew = false
        }
    }

    fun chooseA() = vote(true)
    fun chooseB() = vote(false)

    private fun vote(choseA: Boolean) {
        val d = _current.value ?: return
        if (_isVoting.value) return // double tap koruması
        _isVoting.value = true

        val field = if (choseA) "votesA" else "votesB"
        db.collection("dilemmas").document(d.id)
            .set(mapOf(field to FieldValue.increment(1)), SetOptions.merge())
            .addOnSuccessListener {
                _hasChosen.value = true      // yüzdeleri göster
                _isVoting.value = false
            }
            .addOnFailureListener {
                _isVoting.value = false
            }
    }

    override fun onCleared() {
        super.onCleared()
        listJob?.cancel()
        docListener?.remove()
    }
}