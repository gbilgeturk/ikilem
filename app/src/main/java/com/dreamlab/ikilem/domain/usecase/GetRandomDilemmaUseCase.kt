package com.dreamlab.ikilem.domain.usecase

import com.dreamlab.ikilem.data.model.Category
import com.dreamlab.ikilem.data.model.Dilemma
import com.dreamlab.ikilem.data.repo.DilemmaRepository

class GetRandomDilemmaUseCase(private val repo: DilemmaRepository) {
    operator fun invoke(category: Category?): Dilemma = repo.getRandom(category)
}