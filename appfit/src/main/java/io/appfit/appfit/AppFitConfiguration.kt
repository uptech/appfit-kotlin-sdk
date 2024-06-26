package io.appfit.appfit

/**
 * Configuration for the AppFit SDK.
 *
 * This is used to initialize the SDK with the apiKey provided by AppFit.
 *
 * Example:
 * ```kotlin
 * val configuration = AppFitConfiguration(apiKey = "<key>")
 * ```
 *
 *  @param apiKey: The API key provided by AppFit.
 */
data class AppFitConfiguration(
    /// The API key provided by AppFit.
    val apiKey: String,

    /// The version of the app.
    val appVersion: String? = null,

    /// Enable or disable IP Tracking
    val enableIpTracking: Boolean = true
)