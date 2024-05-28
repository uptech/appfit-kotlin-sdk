package io.appfit.appfit.properties

private const val OPERATING_SYSTEM_NAME = "Android"

data class OperatingSystem(
    /*
     * Operating System Name
     */
    val name: String = OPERATING_SYSTEM_NAME,

    /*
     * Operating System Version
     */
    val version: String,
)
