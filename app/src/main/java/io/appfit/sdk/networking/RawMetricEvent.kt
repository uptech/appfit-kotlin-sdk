package io.appfit.sdk.networking

import java.util.Date

internal const val APPFIT_EVENT_SOURCE = "appfit"

/**
 * A raw metric event that is sent to the AppFit API.
 *
 * This is the raw event that is sent to the AppFit API.
 */
internal data class RawMetricEvent(
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
    val payload: MetricEvent,

    /**
     * The source of the event.
     *
     * This is hardcoded to `appfit` in the SDK.
     */
    val eventSource: String,
)