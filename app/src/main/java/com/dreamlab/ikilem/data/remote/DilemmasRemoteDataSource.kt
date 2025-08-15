package com.dreamlab.ikilem.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.dreamlab.ikilem.data.model.Dilemma
import com.dreamlab.ikilem.data.model.Category
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

object DilemmasRemoteDataSource {
    private val db = FirebaseFirestore.getInstance()

    fun streamByCategory(category: Category?) = callbackFlow<List<Dilemma>> {
        val db = FirebaseFirestore.getInstance()
        var lastGood: List<Dilemma> = emptyList()

        val base = db.collection("dilemmas").whereEqualTo("active", true)
        val query = if (category != null) base.whereEqualTo("category", category.name) else base
            // İndeks hazır değilse bu satırı geçici yorumlayabilirsin
            .orderBy("createdAt", Query.Direction.DESCENDING)

        val reg = query.addSnapshotListener { snap, err ->
            if (err != null) {
                // ❗️Hata geldiğinde son iyi veriyi koru; UI'yı boşaltma
                trySend(lastGood)
                return@addSnapshotListener
            }
            val list = snap?.documents?.mapNotNull { d ->
                val optionA = d.getString("optionA") ?: return@mapNotNull null
                val optionB = d.getString("optionB") ?: return@mapNotNull null
                val catName = d.getString("category") ?: return@mapNotNull null
                val cat = Category.entries.firstOrNull { it.name == catName } ?: return@mapNotNull null
                Dilemma(
                    id = d.id,
                    category = cat,
                    optionA = optionA,
                    optionB = optionB,
                    description = d.getString("description"),
                    nsfw = d.getBoolean("nsfw") ?: false
                )
            }.orEmpty()

            lastGood = list
            trySend(list)
        }
        awaitClose { reg.remove() }
    }
}