package com.sakethh.jetspacer.domain.useCase

import com.sakethh.jetspacer.data.repository.ExploreScreenRelatedAPIsImplementation
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.NASAImageLibrarySearchModifiedDTO
import com.sakethh.jetspacer.domain.model.nasa_image_search.NASAImageLibrarySearchDTO
import com.sakethh.jetspacer.domain.repository.ExploreScreenRelatedAPIsRepository
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchImagesFromNasaImageLibraryUseCase(private val exploreScreenRelatedAPIsRepository: ExploreScreenRelatedAPIsRepository = ExploreScreenRelatedAPIsImplementation()) {
    operator fun invoke(
        query: String,
        page: Int
    ): Flow<Response<List<NASAImageLibrarySearchModifiedDTO>>> {
        return flow {
            emit(Response.Loading())
            val httpResponse = exploreScreenRelatedAPIsRepository.getResultsFromNASAImageLibrary(
                query,
                page
            )
            if (httpResponse.status.isSuccess().not()) {
                emit(
                    Response.Failure(
                        exceptionMessage = "Network request failed.",
                        statusCode = httpResponse.status.value,
                        statusDescription = httpResponse.status.description
                    )
                )
                return@flow
            }
            try {
                val retrievedCollectionData =
                    httpResponse.body<NASAImageLibrarySearchDTO>().collection
                val modifiedCollectionData =
                    retrievedCollectionData.copy(items = retrievedCollectionData.items.filter { it.data.any { it.media_type == "image" } })
                emit(
                    Response.Success(
                        List(modifiedCollectionData.items.size) {
                            NASAImageLibrarySearchModifiedDTO(
                                keywords = modifiedCollectionData.items[it].data.first().keywords,
                                dateCreated = modifiedCollectionData.items[it].data.first().date_created,
                                location = modifiedCollectionData.items[it].data.first().location,
                                photographer = modifiedCollectionData.items[it].data.first().photographer,
                                title = modifiedCollectionData.items[it].data.first().title,
                                description = modifiedCollectionData.items[it].data.first().description,
                                nasaId = modifiedCollectionData.items[it].data.first().nasa_id,
                                imageUrl = modifiedCollectionData.items[it].links.find { it.render == "image" }?.href
                                    ?: ""
                            )
                        }
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                emit(
                    Response.Failure(
                        exceptionMessage = e.message.toString(),
                        statusCode = httpResponse.status.value,
                        statusDescription = httpResponse.status.description
                    )
                )
            }
        }
    }
}