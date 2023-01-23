package ru.khozyainov.splashun.data.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class OnBoardingRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : OnBoardingRepository {

    override suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = completed
        }
    }

    override fun readOnBoardingState(): Flow<Boolean> = dataStore
        .data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[ONBOARDING_COMPLETED] ?: false
        }

    companion object {
        private val ONBOARDING_COMPLETED = booleanPreferencesKey("ONBOARDING_COMPLETED")
    }
}