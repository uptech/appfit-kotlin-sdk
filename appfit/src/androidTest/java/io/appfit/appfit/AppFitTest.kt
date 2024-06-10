package io.appfit.appfit

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.appfit.appfit.networking.Digestible
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.impl.annotations.SpyK
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class EventDigesterStub: Digestible {
    override fun digest(event: AppFitEvent) { }
    override fun identify(userId: String?) { }
}

class AppFitTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @SpyK
    val digesterSpy = spyk<EventDigesterStub>()

    private val appFit = AppFit(
        context = context,
        configuration = AppFitConfiguration(apiKey = "", appVersion = null),
        digester = digesterSpy
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testTrackingEvent() {
        clearMocks(digesterSpy)

        appFit.trackEvent(
            name = "unit_test",
            properties = mapOf("language" to "kotlin")
        )

        verify(exactly = 1) {
            digesterSpy.digest(any())
        }
    }

    @Test
    fun testIdentifyingUser() {
        clearMocks(digesterSpy)

        appFit.identifyUser("test_user")

        verify(exactly = 1) {
            digesterSpy.identify(any())
        }
    }
}