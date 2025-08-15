package com.dreamlab.ikilem.data.repo

import android.content.Context
import com.dreamlab.ikilem.data.local.CategoriesLocalDataSource
import com.dreamlab.ikilem.data.local.DilemmasLocalDataSource
import com.dreamlab.ikilem.data.model.Category
import com.dreamlab.ikilem.data.model.Dilemma
import com.dreamlab.ikilem.data.remote.DilemmasRemoteDataSource
import kotlinx.coroutines.flow.Flow

class DilemmaRepository(val applicationContext: Context) {
    // Kullanıcı seçimlerini local memory'de tutan basit sayaç (v1). İleride DataStore/Room/Firebase'e taşınır.
    private val choiceStats: MutableMap<String, Pair<Int, Int>> = mutableMapOf()

    fun submitChoice(dilemmaId: String, choseA: Boolean) {
        val (a, b) = choiceStats[dilemmaId] ?: (0 to 0)
        choiceStats[dilemmaId] = if (choseA) (a + 1) to b else a to (b + 1)
    }

    fun getStats(dilemmaId: String): Pair<Int, Int> = choiceStats[dilemmaId] ?: (0 to 0)

    fun categoryOverview() = DilemmasLocalDataSource.allCategoriesCount()

    private val recentlyShown = ArrayDeque<String>()
    private val maxMemory = 5

    fun getRandom(category: Category?): Dilemma {
        var d: Dilemma
        var tries = 0
        do {
            d = DilemmasLocalDataSource.random(category)
            tries++
        } while (recentlyShown.contains(d.id) && tries < 10)

        recentlyShown.addLast(d.id)
        if (recentlyShown.size > maxMemory) recentlyShown.removeFirst()
        return d
    }

    // ---- Kategoriler: assets'ten (sabit) ----
    data class CategoryDisplay(val category: Category, val title: String)
    fun loadCategories(): List<CategoryDisplay> =
        CategoriesLocalDataSource.load(applicationContext).mapNotNull { c ->
            val enum = CategoriesLocalDataSource.toEnum(c.code) ?: return@mapNotNull null
            CategoryDisplay(enum, c.title)
        }

    // ---- Sorular: Firestore canlı ----
    fun streamDilemmas(category: Category?): Flow<List<Dilemma>> =
        DilemmasRemoteDataSource.streamByCategory(category)
}