package io.appfit.appfit

import java.util.Date
import java.util.UUID

/**
 * An event that can be sent to AppFit
 *
 * Each event contains all of the metadata for tracking.
 * This contains all of the data from the parameters below.
 *
 * @param name: The name of the event.
 * @param properties: The properties of the event.
 */
data class AppFitEvent(
    /// The unique identifier of the event
    internal val id: UUID = UUID.randomUUID(),

    /// The date of the event
    internal val date: Date = Date(),

    /// The name of the event
    val name: String,

    /// The properties for the event
    val properties: Map<String, String>? = null
)