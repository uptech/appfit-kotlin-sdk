package io.appfit.appfit.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.appfit.appfit.cache.AppFitCache.Companion.ANONYMOUS_ID
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

    suspend fun getAnonymousId(): String? {
        val settings = context.dataStore.data.first()
        return settings[ANONYMOUS_ID] ?: return generateAnonymousId()
    }

    private suspend fun generateAnonymousId(): String {
        val id = UUID.randomUUID().toString()
        context.dataStore.edit { settings ->
            settings[ANONYMOUS_ID] = id
        }
        return id
    }
}