package io.appfit.appfit.networking

/**
 * Batch Raw Metric Events
 *
 * This creates an a new ``BatchMetricEvent`` model that contains
 * an array of ``RawMetricEvent's``. This allows you to send a bulk
 * list of events to the API.
 */
internal data class BatchMetricEvents(
    /**
     * The list of events
     */
    val events: List<MetricEvent>,
)