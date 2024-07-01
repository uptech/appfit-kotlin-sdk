package io.appfit.appfit.cache

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.appfit.appfit.networking.MetricEvent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readText
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import java.util.Date

internal class IPAddress(
    /// The HTTP Engine to use for the request
    engine: HttpClientEngine = CIO.create()
) {
    var address: String? = null
    private var lastUpdatedAt: Date = Date()

    private val client = HttpClient(engine) {
        install(HttpTimeout) {
            requestTimeoutMillis = 3000
        }
        defaultRequest {
            url("https://api.ipgeolocation.io")
            contentType(ContentType.Application.Json)
        }
    }

    private fun getConfiguredGsonSerializer(): Gson {
        return GsonBuilder()
            .create()
    }

    private fun isExpired(): Boolean {
        return Date().time - lastUpdatedAt.time > 1000 * 60 * 60
    }

    suspend fun fetchIpAddress(): String? {
        if (!isExpired()) {
            return address
        }

        val gson = getConfiguredGsonSerializer()

        val response: HttpResponse = client.get {
            url {
                appendPathSegments("getip")
            }
        }

        if (response.status.value in 200..299) {
            val parsed = gson.fromJson(response.bodyAsText(), Address::class.java)
            address = parsed.ip
            lastUpdatedAt = Date()
            return parsed.ip
        }
        return null
    }
}

data class Address(
    val ip: String
)