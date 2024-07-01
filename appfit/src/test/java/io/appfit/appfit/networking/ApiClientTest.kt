package io.appfit.appfit.networking

import android.os.Build
import com.google.gson.GsonBuilder
import io.appfit.appfit.cache.IPAddress
import io.appfit.appfit.properties.DeviceProperties
import io.appfit.appfit.properties.EventSystemProperties
import io.appfit.appfit.properties.OperatingSystem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class ApiClientTest {
    private val apiClient = ApiClient(apiKey = "YjZiODczMjItNTAwNC00YTg5LTg2ZTUtOWI3OWE5ZDA5Mjc3OmQ3OGMyMjVhLTc1YzQtNDY5ZC1iZTk5LTY3ZTZiMWM1ZDI5YQ==")

    @Test
    fun testSingleEvent() = runTest {
        val ipAddress = IPAddress().fetchIpAddress()

        val event = MetricEvent(
            occurredAt = Date(),
            payload = EventPayload(
                sourceEventId = UUID.randomUUID(),
                eventName = "unit_test",
                userId = null,
                anonymousId = "android_studio_75fbf7a3-2197-4353-9b39-eeadf4628c68",
                properties = mapOf("language" to "kotlin"),
                systemProperties = EventSystemProperties(
                    appVersion = "1.0.0",
                    ipAddress = ipAddress,
                    device = DeviceProperties(
                        manufacturer = "Unit",
                        model = "Test"
                    ),
                    operatingSystem = OperatingSystem(
                        version = "14"
                    )
                )
            )
        )
        advanceUntilIdle()
        val result = apiClient.send(event)

        Assert.assertEquals(true, result)
    }

    @Test
    fun testBatchEvents() = runTest {
        val ipAddress = IPAddress().fetchIpAddress()

        val event = MetricEvent(
            occurredAt = Date(),
            payload = EventPayload(
                sourceEventId = UUID.randomUUID(),
                eventName = "unit_test",
                userId = null,
                anonymousId = "android_studio_75fbf7a3-2197-4353-9b39-eeadf4628c68",
                properties = mapOf("language" to "kotlin"),
                systemProperties = EventSystemProperties(
                    appVersion = "1.0.0",
                    ipAddress = ipAddress,
                    device = DeviceProperties(
                        manufacturer = "Unit",
                        model = "Test"
                    ),
                    operatingSystem = OperatingSystem(
                        version = "14"
                    )
                )
            )
        )
        advanceUntilIdle()
        val result = apiClient.send(listOf(event))

        Assert.assertEquals(true, result)
    }
}