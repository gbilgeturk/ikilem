package com.dreamlab.ikilem.domain.usecase

import com.dreamlab.ikilem.data.repo.DilemmaRepository

class SubmitChoiceUseCase(private val repo: DilemmaRepository) {
    operator fun invoke(dilemmaId: String, choseA: Boolean) = repo.submitChoice(dilemmaId, choseA)
}