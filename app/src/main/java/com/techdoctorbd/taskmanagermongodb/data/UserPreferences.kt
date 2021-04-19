package com.techdoctorbd.taskmanagermongodb.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(context: Context) {

    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> =
        applicationContext.createDataStore(name = "user_data_store")


    val authToken: Flow<String?>
        get() = dataStore.data.map { it[KEY_AUTH] }

    suspend fun saveAuthToken(authToken: String) {
        dataStore.edit {
            it[KEY_AUTH] = authToken
        }
    }

    companion object {
        private val KEY_AUTH = stringPreferencesKey("key_auth")
    }
}