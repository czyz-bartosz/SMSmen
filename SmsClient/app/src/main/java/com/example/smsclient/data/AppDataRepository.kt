package com.example.smsclient.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class AppDataRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val SERVER_URL = stringPreferencesKey("server_url")
    }

    suspend fun saveServerUrl(serverUrl: String) {
        dataStore.edit { preferences ->
            preferences[SERVER_URL] = serverUrl
        }
    }

    val serverUrl: Flow<String> = dataStore.data
        .catch {
            emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[SERVER_URL] ?: ""
        }
}