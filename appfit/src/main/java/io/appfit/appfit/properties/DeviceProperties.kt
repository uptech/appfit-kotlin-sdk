package io.appfit.appfit.properties

/**
 * Device Properties
 *
 * These includes all of the metadata related to the device
 */
internal data class DeviceProperties(
    /*
     * The Device Manufacturer (Apple)
     */
    val manufacturer: String,

    /*
     * The Device Model (MacBookPro18,4)
     */
    val model: String,
)
