package io.appfit.sdk.cache

import android.content.Context
import android.icu.lang.UProperty.AGE
import android.service.autofill.UserData
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.util.UUID

internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "appfit")

internal class AppFitCache(
    private val context: Context
) {
    companion object {
        val USER_ID = stringPreferencesKey("userId")
        val ANONYMOUS_ID = stringPreferencesKey("anonymousId")
    }

    suspend fun saveUserId(userId: String?) {
        context.dataStore.edit { settings ->
            if (settings[USER_ID] == null) {
                settings.remove(USER_ID)
            } else {
                settings[USER_ID] = userId!!
            }
        }
    }

    suspend fun getUserId(): String? {
        val settings = context.dataStore.data.first()
        return settings[USER_ID]
    }

    suspend fun generateAnonymousId(): String {
        val currentId = getAnonymousId()
        val id = UUID.randomUUID().toString()
        if (currentId == null) {
            context.dataStore.edit { settings ->
                settings[ANONYMOUS_ID] = id
            }
            return id
        }
        return currentId
    }

    suspend fun getAnonymousId(): String? {
        val settings = context.dataStore.data.first()
        return settings[ANONYMOUS_ID]
    }
}