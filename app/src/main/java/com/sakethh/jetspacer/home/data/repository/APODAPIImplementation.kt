package com.sakethh.jetspacer.home.data.repository

import com.sakethh.jetspacer.common.network.HTTPClient
import com.sakethh.jetspacer.home.domain.model.APODDTO
import com.sakethh.jetspacer.home.domain.repository.APODAPIRepository
import io.ktor.client.call.body
import io.ktor.client.request.get

class APODAPIImplementation : APODAPIRepository {
    override suspend fun getAPODDataFromTheAPI(): APODDTO {
        return HTTPClient.ktorClient.get("https://api.nasa.gov/planetary/apod?api_key=bCUlmKS9EpyQ1ChMIUnsglSTGTwQy1CdLhLP4FCL")
            .body()
    }
}