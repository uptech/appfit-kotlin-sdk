package io.appfit.sdk


/// Configuration for the AppFit SDK.
///
/// This is used to initialize the SDK with the apiKey provided by AppFit.
///
/// Example:
/// ```kotlin
/// val configuration = AppFitConfiguration(apiKey = "<key>")
/// ```
///
/// - Parameters:
///   - apiKey: The API key provided by AppFit.
data class AppFitConfiguration(
    /// The API key provided by AppFit.
    val apiKey: String
)