package com.dreamlab.ikilem.data.seed

import android.content.Context
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import org.json.JSONArray

object Seeder {
    private const val TAG = "Seeder"

    data class SeedItem(
        val id: String,
        val category: String,
        val optionA: String,
        val optionB: String,
        val active: Boolean,
        val votesA: Int,
        val votesB: Int
    )

    private fun readSeed(context: Context): List<SeedItem> {
        val json = context.assets.open("dilemmas_seed.json").bufferedReader().use { it.readText() }
        val arr = JSONArray(json)
        val out = ArrayList<SeedItem>(arr.length())
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            out.add(
                SeedItem(
                    id = o.getString("id"),
                    category = o.getString("category"),
                    optionA = o.getString("optionA"),
                    optionB = o.getString("optionB"),
                    active = o.getBoolean("active"),
                    votesA = o.optInt("votesA", 0),
                    votesB = o.optInt("votesB", 0)
                )
            )
        }
        return out
    }

    fun seedIfNeeded(context: Context, force: Boolean = false, onDone: (Boolean) -> Unit = {}) {
        val db = FirebaseFirestore.getInstance()
        val col = db.collection("dilemmas")

        col.limit(1).get().addOnSuccessListener { snap ->
            if (!force && !snap.isEmpty) {
                Log.d(TAG, "Seed atlanıyor: koleksiyon boş değil.")
                onDone(false)
                return@addOnSuccessListener
            }

            val items = readSeed(context)
            val chunks = items.chunked(400) // güvenli batch boyutu

            fun writeChunk(index: Int = 0) {
                if (index >= chunks.size) {
                    Log.d(TAG, "Seed bitti. Toplam doc: ${items.size}")
                    onDone(true)
                    return
                }
                val batch = db.batch()
                for (it in chunks[index]) {
                    val ref = col.document(it.id)
                    val data = hashMapOf(
                        "category" to it.category,
                        "optionA" to it.optionA,
                        "optionB" to it.optionB,
                        "active" to it.active,
                        "votesA" to it.votesA,
                        "votesB" to it.votesB,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    batch.set(ref, data, SetOptions.merge())
                }
                batch.commit()
                    .addOnSuccessListener {
                        Log.d(TAG, "Chunk ${index+1}/${chunks.size} tamam")
                        writeChunk(index + 1)
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Seed chunk hata", e)
                        onDone(false)
                    }
            }

            writeChunk(0)
        }.addOnFailureListener {
            Log.e(TAG, "Koleksiyon kontrolü hatası", it)
            onDone(false)
        }
    }
}