package com.dreamlab.ikilem.ui.debug

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FirestoreDebugViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _status = MutableStateFlow("Hazır")
    val status: StateFlow<String> = _status

    private val _msg = MutableStateFlow<String?>(null)
    val msg: StateFlow<String?> = _msg

    // Tek seferlik yazma
    fun writeTest() {
        _status.value = "Yazılıyor…"
        db.collection("debug").document("hello")
            .set(mapOf("msg" to "Merhaba"))
            .addOnSuccessListener { _status.value = "Yazıldı ✅" }
            .addOnFailureListener { e -> _status.value = "Yazma Hatası: ${e.message}" }
    }

    // Canlı dinleme (listener hep açık kalır)
    fun startListening() {
        _status.value = "Dinleme başlatıldı…"
        db.collection("debug").document("hello")
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    _status.value = "Dinleme Hatası: ${err.message}"
                    return@addSnapshotListener
                }
                val text = snap?.getString("msg")
                _msg.value = text
                _status.value = "Son belge alındı ✅"
            }
    }
}