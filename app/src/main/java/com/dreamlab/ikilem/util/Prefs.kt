package com.dreamlab.ikilem.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "ikilem_prefs")

class Prefs(private val context: Context) {
    val onboardingSeen: Flow<Boolean> = context.dataStore.data.map { it[DataStoreKeys.ONBOARDING_SEEN] ?: false }
    suspend fun setOnboardingSeen(value: Boolean) {
        context.dataStore.edit { it[DataStoreKeys.ONBOARDING_SEEN] = value }
    }
}