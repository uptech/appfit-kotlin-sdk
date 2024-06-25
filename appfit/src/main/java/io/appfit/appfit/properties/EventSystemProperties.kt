package io.appfit.appfit.properties

import com.google.gson.annotations.SerializedName

internal data class EventSystemProperties(
    /*
     * The version of the parent application.
     */
    @SerializedName("appVersion")
    val appVersion: String?,

    /*
     * The IP Address of the device
     */
    @SerializedName("ipAddress")
    val ipAddress: String?,

    /* All of the device related properties
     * These include anything that is specific to the physical device
     * such as model, operating system version, platform, etc
     */
    @SerializedName("device")
    val device: DeviceProperties?,

    /* All of the operating system properties
     * These include the operating system name (if present) and the version number.
     */
    @SerializedName("os")
    val operatingSystem: OperatingSystem?
)