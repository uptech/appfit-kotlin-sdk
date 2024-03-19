package io.appfit.sdk.networking

import java.util.UUID

/**
 * A metric event that is sent to the AppFit API.
 *
 * This is the event that is sent to the AppFit API.
 */
internal data class MetricEvent(
    /**
     * The unique identifier for the event.
     */
    val eventId: UUID,

    /**
     * The name of the event.
     */
    val name: String,

    /**
     * The user identifier for the event.
     */
    val userId: String?,

    /**
     * The anonymous identifier for the event.
     */
    val anonymousId: String?,

    /**
     * The properties of the event.
     */
    val properties: Map<String, String>?,

    /**
     * The system properties of the event.
     */
    val systemProperties: Map<String, String>?
)