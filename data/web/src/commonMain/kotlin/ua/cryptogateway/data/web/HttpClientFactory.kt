package ua.cryptogateway.data.web

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log

@Inject
class HttpClientFactory {
    fun build(): HttpClient = HttpClient(engineFactory = CIO).config {
        install(DefaultRequest) {
            url(BuildConfig.KUNA_BASE_URL)
            header("accept", "application/json")
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) = Log.debug(tag = "HttpClient") { message }
            }
        }
    }
}