package com.sakethh.jetspacer.domain.useCase

import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.NASAImageLibrarySearchDTOFlatten
import com.sakethh.jetspacer.domain.model.nasa_image_search.NASAImageLibrarySearchDTO
import com.sakethh.jetspacer.domain.repository.NasaRepository
import com.sakethh.jetspacer.ui.utils.extractBodyFlow
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow

class FetchImagesFromNasaImageLibraryUseCase(private val nasaRepository: NasaRepository) {
    suspend operator fun invoke(
        query: String, page: Int
    ): Flow<Response<List<NASAImageLibrarySearchDTOFlatten>>> {
        return extractBodyFlow(
            httpResponse = nasaRepository.searchImageLibrary(
                query, page
            )
        ) { httpResponse ->
            val retrievedCollectionData = httpResponse.body<NASAImageLibrarySearchDTO>().collection
            val modifiedCollectionData =
                retrievedCollectionData.copy(items = retrievedCollectionData.items.filter { it.data.any { it.media_type == "image" } })
            List(modifiedCollectionData.items.size) {
                NASAImageLibrarySearchDTOFlatten(
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
        }
    }
}