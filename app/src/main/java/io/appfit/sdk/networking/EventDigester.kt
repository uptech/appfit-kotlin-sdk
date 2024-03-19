package io.appfit.sdk.networking

import io.appfit.sdk.AppFitEvent

/**
 * EventDigester takes in events, and handles the caching, posting, and
 * retrying of failed events.
 */
internal class EventDigester(
    apiKey: String
) {
    private val apiClient = ApiClient(apiKey = apiKey)

    init {

    }

    /**
     * Digests the event.
     *
     * This is used to digest the event and send it to the AppFit API.
     */
    fun digest(event: AppFitEvent) {
        // Digest the event
    }

    /**
     * Identifies the user.
     *
     * This is used to identify the user in the AppFit API.
     */
    fun identify(userId: String?) {
        // Identify the user
    }

    /**
     * Digests the cached events.
     */
    fun digestCachedEvents() {
        // Digest the cached events
    }
}