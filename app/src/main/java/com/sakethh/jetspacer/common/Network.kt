package com.sakethh.jetspacer.common

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object Network {
    @OptIn(ExperimentalSerializationApi::class)
    val ktorClient = HttpClient(Android) {
        install(ContentNegotiation.Plugin) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                explicitNulls = false
            })
        }
    }
}