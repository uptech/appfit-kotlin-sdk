package io.appfit.appfit.networking

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType

internal class ApiClient(
    /// The API key provided by AppFit.
    apiKey: String,

    /// The base URL of the request
    baseUrl: String = "https://api.appfit.io",

    /// The HTTP Engine to use for the request
    engine: HttpClientEngine = CIO.create()
) {
    private val client = HttpClient(engine) {
        install(HttpTimeout) {
            requestTimeoutMillis = 3000
        }
        HttpResponseValidator {
            validateResponse { response: HttpResponse ->
                val statusCode = response.status.value
                when (statusCode) {
                    in 300..399 -> Log.e("ApiClient", "300-399: $response")
                    in 400..499 -> Log.e("ApiClient", "400-499: $response")
                    in 500..599 -> Log.e("ApiClient", "500-599: $response")
                }

                if (statusCode >= 600) {
                    Log.e("ApiClient", "500-599: $response")
                }
            }
            handleResponseExceptionWithRequest { request, cause ->
                Log.e("ApiClient", "Request: $request")
                Log.e("ApiClient", "Cause: $cause")
            }
        }
        defaultRequest {
            url(baseUrl)
            contentType(ContentType.Application.Json)
            header("Authorization", "Basic $apiKey")
        }
    }

    private fun getConfiguredGsonSerializer(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()
    }

    suspend fun send(event: RawMetricEvent): Boolean{
        val gson = getConfiguredGsonSerializer()

        val response: HttpResponse = client.post {
            url {
                appendPathSegments("metric-events")
            }
            setBody(gson.toJson(event, RawMetricEvent::class.java))
        }

        return response.status.value in 200..299
    }

    suspend fun send(events: List<RawMetricEvent>): Boolean {
        val gson = getConfiguredGsonSerializer()

        val response: HttpResponse = client.post {
            url {
                appendPathSegments("metric-events", "batch")
            }
            setBody(gson.toJson(BatchRawMetricEvents(events), BatchRawMetricEvents::class.java))
        }

        return response.status.value in 200..299
    }
}