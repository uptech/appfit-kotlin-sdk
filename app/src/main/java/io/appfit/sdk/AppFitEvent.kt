package io.appfit.sdk

import java.util.Date
import java.util.UUID


/// An event that can be sent to AppFit
///
///Each event contains all of the metadata for tracking.
/// This contains all of the data from the parameters below.
data class AppFitEvent(
    /// The unique identifier of the event
    private val id: UUID = UUID.randomUUID(),

    /// The date of the event
    private val date: Date = Date(),

    /// The name of the event
    val name: String,

    /// The properties for the event
    val properties: Map<String, String>? = null
)