package io.appfit.appfit.networking

import android.content.Context
import android.os.Build
import io.appfit.appfit.AppFitEvent
import io.appfit.appfit.cache.AppFitCache
import io.appfit.appfit.cache.EventCache
import io.appfit.appfit.properties.DeviceProperties
import io.appfit.appfit.properties.EventSystemProperties
import io.appfit.appfit.properties.OperatingSystem
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

interface Digestible {
    fun digest(event: AppFitEvent)
    fun identify(userId: String?)
}

/**
 * EventDigester takes in events, and handles the caching, posting, and
 * retrying of failed events.
 */
@OptIn(DelicateCoroutinesApi::class)
internal class EventDigester(
    val context: Context,
    apiKey: String
): Digestible {
    private val appFitCache = AppFitCache(context = context)
    private val cache = EventCache()
    private val apiClient = ApiClient(apiKey = apiKey)

    init {
        Executors.newSingleThreadScheduledExecutor().schedule({
            digestCachedEvents()
        }, 15, TimeUnit.MINUTES)
    }

    /**
     * Digests the event.
     *
     * This is used to digest the event and send it to the AppFit API.
     */
    override
    fun digest(event: AppFitEvent) {
        // Digest the event
        GlobalScope.launch {
            val userId = appFitCache.getUserId()
            val anonymousId = appFitCache.getAnonymousId()
            val rawMetricEvent = createRawMetricEvent(event, userId, anonymousId)
            val result = apiClient.send(rawMetricEvent)
            when (result) {
                true -> {
                    // For now, we just do nothing.
                }
                false -> {
                    // Add the event to the cache
                    cache.add(event)
                }
            }
        }
    }

    /**
     * Identifies the user.
     *
     * This is used to identify the user in the AppFit API.
     */
    override
    fun identify(userId: String?) {
        // Identify the user
        GlobalScope.launch {
            appFitCache.saveUserId(userId)
        }
    }

    /**
     * Digests the cached events.
     */
    private fun digestCachedEvents() {
        // Digest the cached events
        GlobalScope.launch {
            val userId = appFitCache.getUserId()
            val anonymousId = appFitCache.getAnonymousId()

            // Get the cached events
            val events = cache.events.map { event ->
                createRawMetricEvent(event, userId, anonymousId)
            }

            // Send the events to the AppFit API
            val result = apiClient.send(events)
            when (result) {
                true -> {
                    // Remove the events from the cache
                    cache.clear()
                }
                false -> {
                    // In this case we need to do nothing
                }
            }
        }
    }

    private fun createRawMetricEvent(event: AppFitEvent, userId: String?, anonymousId: String?): MetricEvent {
        val versionString = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        return MetricEvent(
            occurredAt = event.date,
            payload = EventPayload(
                sourceEventId = event.id,
                eventName = event.name,
                userId = userId,
                anonymousId = anonymousId,
                properties = event.properties,
                systemProperties = EventSystemProperties(
                    appVersion = versionString,
                    device = DeviceProperties(
                        manufacturer = Build.MANUFACTURER,
                        model = Build.MODEL
                    ),
                    operatingSystem = OperatingSystem(
                        version = Build.VERSION.RELEASE
                    )
                )
            )
        )
    }
}