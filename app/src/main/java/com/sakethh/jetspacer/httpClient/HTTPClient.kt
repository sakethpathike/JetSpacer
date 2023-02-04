package com.sakethh.jetspacer.httpClient

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.cache.storage.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Paths

object HTTPClient {
    @RequiresApi(Build.VERSION_CODES.O)
    val ktorClientWithCache=HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }

        /*install(HttpCache){
        val cacheFile = Files.createDirectories(Paths.get("app/cache")).toFile()
            publicStorage(FileStorage(cacheFile))
        }*/
    }
   val ktorClientWithoutCache = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                json(Json {
                    coerceInputValues = true
                ignoreUnknownKeys = true
            })
        })
    }
   }
}