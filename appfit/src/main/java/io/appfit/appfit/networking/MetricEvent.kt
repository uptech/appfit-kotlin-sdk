package io.appfit.appfit.networking

import java.util.Date

private const val APPFIT_EVENT_SOURCE = "appfit"
private const val PAYLOAD_VERSION = "2"

/**
 * A raw metric event that is sent to the AppFit API.
 *
 * This is the raw event that is sent to the AppFit API.
 */
internal data class MetricEvent(
    /**
    * The name of the event.
    */
    val version: String = PAYLOAD_VERSION,

    /**
     * The source of the event.
     *
     * This is hardcoded to `appfit` in the SDK.
     */
    val eventSource: String = APPFIT_EVENT_SOURCE,

    /**
     * The time the event occurred.
     *
     * This is a UTC timestamp in ISO-8601 format.
     */
    val occurredAt: Date,

    /**
     * The event payload.
     *
     * This is the event that is tracked by AppFit.
     */
    val payload: EventPayload,
)