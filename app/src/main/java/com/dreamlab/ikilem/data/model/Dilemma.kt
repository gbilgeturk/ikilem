package com.dreamlab.ikilem.data.model

data class Dilemma(
    val id: String,
    val category: Category,
    val optionA: String,
    val optionB: String,
    val description: String? = null,
    val nsfw: Boolean = false
)