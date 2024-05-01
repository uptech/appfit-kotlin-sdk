package io.appfit.appfit.networking

import android.content.Context
import io.appfit.appfit.AppFitEvent
import io.appfit.appfit.cache.AppFitCache
import io.appfit.appfit.cache.EventCache
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
    context: Context,
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

    private fun createRawMetricEvent(event: AppFitEvent, userId: String?, anonymousId: String?): RawMetricEvent {
        // For now, we are going to hard-code the system properties with the one key that we need.
        // Eventually we need to make this dynamic and move this to another place as we will be
        // fetching system properties of the device.
        val systemProperties: Map<String, Any> = mapOf("origin" to "kotlin")
        
        return RawMetricEvent(
            occurredAt = event.date,
            eventSource = APPFIT_EVENT_SOURCE,
            payload = MetricEvent(
                eventId = event.id,
                name = event.name,
                properties = event.properties,
                systemProperties = systemProperties,
                userId = userId,
                anonymousId = anonymousId,
            )
        )
    }
}