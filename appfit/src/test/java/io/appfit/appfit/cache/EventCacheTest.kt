package io.appfit.appfit.cache

import android.os.Environment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.appfit.appfit.AppFitEvent
import org.junit.Test
import java.io.File
import java.util.concurrent.TimeUnit

class EventCacheTest {
    @Test
    fun testWritingToCache() {
        val cache = EventCache(
            writeToDiskInterval = 10,
            timeUnit = TimeUnit.SECONDS
        )
        cache.add(AppFitEvent(name = "test"))
        assert(cache.events.size == 1)

        // Sleep for 15 seconds to allow the cache to write to disk
        Thread.sleep(10 * 1000)

        val cacheFile = File(Environment.DIRECTORY_DOCUMENTS, "appfit/cache.af")
        val gson = getConfiguredGsonSerializer()

        val json = cacheFile.readText()
        val events = gson.fromJson(json, Array<AppFitEvent>::class.java)
        assert(cacheFile.exists())
        assert(events.size == 1)
    }

    @Test
    fun testReadingCache() {
        val cacheFile = File(Environment.DIRECTORY_DOCUMENTS, "appfit/cache.af")
        if (cacheFile.parentFile?.exists() == false) {
            cacheFile.parentFile?.mkdirs()
        }

        if (!cacheFile.exists()) {
            cacheFile.createNewFile()
        }

        val gson = getConfiguredGsonSerializer()
        val json = gson.toJson(listOf(AppFitEvent(name = "test")))
        cacheFile.writeText(json)

        assert(cacheFile.exists())

        val cache = EventCache(
            writeToDiskInterval = 10,
            timeUnit = TimeUnit.SECONDS
        )

        // Sleep for 15 seconds to allow the cache to read from disk
        Thread.sleep(5 * 1000)
        assert(cache.events.size == 1)
    }

    @Test
    fun validateCacheFunctions() {
        val cache = EventCache(
            writeToDiskInterval = 10,
            timeUnit = TimeUnit.MINUTES
        )
        val event = AppFitEvent(name = "test")

        cache.add(event)
        assert(cache.events.size == 1)

        cache.remove(event)
        assert(cache.events.isEmpty())

        cache.add(event)
        assert(cache.events.size == 1)

        cache.remove(event.id.toString())
        assert(cache.events.isEmpty())

        cache.add(event)
        assert(cache.events.size == 1)

        cache.clear()
        assert(cache.events.isEmpty())
    }

    private fun getConfiguredGsonSerializer(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()
    }
}