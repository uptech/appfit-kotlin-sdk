package io.appfit.appfit.networking

import io.appfit.appfit.properties.EventSystemProperties
import java.util.UUID

private const val PAYLOAD_VERSION = "2"
private const val APPFIT_ORIGIN = "kotlin"

/**
 * A metric event that is sent to the AppFit API.
 *
 * This is the event that is sent to the AppFit API.
 */
internal data class EventPayload(
    /**
     * The name of the event.
     */
    val version: String = PAYLOAD_VERSION,

    /**
     * The unique identifier for the event.
     */
    val sourceEventId: UUID,

    /**
     * The name of the event.
     */
    val eventName: String,

    /**
     * The origin of the event.
     * This is the hard coded value the SDK sends. This will be hard coded as "kotlin".
     */
    val origin: String = APPFIT_ORIGIN,

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
    val properties: Map<String, Any>?,

    /**
     * The system properties of the event.
     */
    val systemProperties: EventSystemProperties?
)