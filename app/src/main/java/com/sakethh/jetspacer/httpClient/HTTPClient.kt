package com.sakethh.jetspacer.httpClient

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import io.ktor.client.*
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.cache.storage.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Paths

object HTTPClient {
    val ktorClientWithCache = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }

        engine {
            this.connectTimeout = 0
        }
        install(Logging) {
            this.level = LogLevel.ALL
            this.logger = object : Logger {
                override fun log(message: String) {
                    Log.d("ktorClientWithCache Logs", message)
                }
            }
        }
    }
    val ktorClientWithoutCache = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                json(Json {
                    coerceInputValues = true
                    ignoreUnknownKeys = true
                })
            })
        }

        engine {
            this.connectTimeout = 0
        }
        install(Logging) {
            this.level = LogLevel.ALL
            this.logger = object : Logger {
                override fun log(message: String) {
                    Log.d("ktorClientWithoutCache Logs", message)
                }
            }
        }
    }
}