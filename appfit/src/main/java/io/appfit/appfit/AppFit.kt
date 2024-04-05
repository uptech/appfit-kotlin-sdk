@file:Suppress("unused")

package io.appfit.appfit

import android.content.Context
import io.appfit.appfit.networking.EventDigester

/**
 * AppFit handles all of the event tracking for the AppFit dashboard.
 *
 * To use the AppFit SDK, you must first initialize it with an ``AppFitConfiguration``.
 */
@Suppress("MemberVisibilityCanBePrivate")
class AppFit(
    context: Context,
    /// The configuration for the AppFit SDK.
    configuration: AppFitConfiguration
) {
    private val eventDigester = EventDigester(context = context, apiKey = configuration.apiKey)

    init {
        // Once we boot up the AppFit SDK, we need to generate an anonymousId
        // and set the userId to null. This is to ensure that we have the most
        // up-to-date information for the events.
        eventDigester.identify(userId = null)
    }

    /**
     * Tracks an event with the provided name and properties.
     *
     * This is used to track events in the AppFit dashboard.
     *
     *  @param name: The name of the event.
     *  @param properties: The properties of the event.
     */
    fun trackEvent(
        name: String,
        properties: Map<String, Any>? = null
    ) {
        track(event = AppFitEvent(name = name, properties = properties))
    }

    /**
     * Tracks an event with the provided event.
     *
     * This is used to track events in the AppFit dashboard.
     * A event must be an ``AppFitEvent`` and conform to the
     * parameters available on the class.
     *
     * @param event: The event to track.
     */
    fun track(event: AppFitEvent) {
        eventDigester.digest(event = event)
    }

    /**
     * Identifies the user with the provided userId.
     *
     * This is used to identify the user in the AppFit dashboard.
     * If the userId is `null`, the user will be un-identified,
     * resulting in the user being anonymous.
     *
     * @param userId: The unique identifier for the user.
     */
    fun identifyUser(userId: String?) {
        eventDigester.identify(userId = userId)
    }
}
