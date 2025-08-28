package com.example.test_2.data_db.users





import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensão do DataStore no contexto
val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPrefs(private val context: Context) {
    companion object {
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
    }

    val loggedUserName: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[USER_NAME_KEY]
    }

    suspend fun saveLoggedUser(nome: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_NAME_KEY] = nome
        }
    }

    suspend fun clearLoggedUser() {
        context.dataStore.edit { prefs ->
            prefs.remove(USER_NAME_KEY)
        }
    }
}