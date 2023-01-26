package com.seiko.imageloader.demo.util

import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers

val customKtorUrlFetcher = KtorUrlFetcher.Factory {
    Napier.base(DebugAntilog())
    HttpClient(httpEngine) {
        defaultRequest {
            headers {
                append("Custom", "AAA")
            }
        }
        install(Logging) {
            level = LogLevel.HEADERS
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.d(tag = "HttpClient") { message }
                }
            }
        }
    }
}

expect val httpEngine: HttpClientEngine