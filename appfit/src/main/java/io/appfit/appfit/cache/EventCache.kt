package io.appfit.appfit.cache

import android.os.Environment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.appfit.appfit.AppFitEvent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@OptIn(DelicateCoroutinesApi::class)
class EventCache(
    writeToDiskInterval: Long = 5,
    timeUnit: TimeUnit = TimeUnit.MINUTES
) {
    private var cache = mutableListOf<AppFitEvent>()

    internal val events: List<AppFitEvent> get() = cache

    init {
        GlobalScope.launch {
            readFromDisk()
        }

        Executors.newSingleThreadScheduledExecutor().schedule({
            GlobalScope.launch {
                writeToDisk()
            }
        }, writeToDiskInterval, timeUnit)
    }

    internal fun add(event: AppFitEvent) {
        cache.add(event)
    }

    internal fun remove(event: AppFitEvent) {
        remove(event.id.toString())
    }

    internal fun remove(id: String) {
        cache.removeAll { it.id.toString() == id }
    }

    internal fun clear() {
        cache.clear()
    }

    private fun getConfiguredGsonSerializer(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()
    }

    private suspend fun readFromDisk() {
        withContext(Dispatchers.IO) {
            val cacheFile = File(Environment.DIRECTORY_DOCUMENTS, "appfit/cache.af")
            val gson = getConfiguredGsonSerializer()
            if (cacheFile.exists()) {
                val json = cacheFile.readText()
                val events = gson.fromJson(json, Array<AppFitEvent>::class.java)
                cache = events.toMutableList()
            }
        }
    }

    private suspend fun writeToDisk() {
        withContext(Dispatchers.IO) {
            val cacheFile = File(Environment.DIRECTORY_DOCUMENTS, "appfit/cache.af")
            if (cacheFile.parentFile?.exists() == false) {
                cacheFile.parentFile?.mkdirs()
            }

            if (!cacheFile.exists()) {
                cacheFile.createNewFile()
            }

            val gson = getConfiguredGsonSerializer()
            val json = gson.toJson(cache)
            cacheFile.writeText(json)
        }
    }
}