package dev.dr10.autowalsurvey.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.dr10.autowalsurvey.domain.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StorageSurveyCounter @Inject constructor(
    private val context: Context
) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.PREFERENCES_NAME)
        private val SURVEY_COUNTER = intPreferencesKey("survey_counter")
    }

    suspend fun addSurvey() {
        context.dataStore.edit { pref ->
            pref[SURVEY_COUNTER] = (pref[SURVEY_COUNTER] ?: 0) + 1
        }
    }

    suspend fun clearCounter() {
        context.dataStore.edit { pref ->
            pref[SURVEY_COUNTER] = 0
        }
    }

    fun getSurveyCounter(): Flow<Int> = context.dataStore.data.map { prefs -> prefs[SURVEY_COUNTER] ?: 0 }

}