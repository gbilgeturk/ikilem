package com.dreamlab.ikilem.data.local

import android.content.Context
import com.dreamlab.ikilem.data.model.Category
import org.json.JSONArray

data class CategoryItem(val code: String, val title: String)

object CategoriesLocalDataSource {
    fun load(context: Context): List<CategoryItem> {
        val json = context.assets.open("categories.json").bufferedReader().use { it.readText() }
        val arr = JSONArray(json)
        return buildList {
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                add(CategoryItem(o.getString("code"), o.getString("title")))
            }
        }
    }

    fun toEnum(code: String): Category? =
        Category.entries.firstOrNull { it.name == code }
}