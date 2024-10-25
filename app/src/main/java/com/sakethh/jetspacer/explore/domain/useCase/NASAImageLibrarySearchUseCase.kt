package com.sakethh.jetspacer.explore.domain.useCase

import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.explore.data.repository.NASAImageLibrarySearchImplementation
import com.sakethh.jetspacer.explore.domain.model.local.NASAImageLibrarySearchModifiedDTO
import com.sakethh.jetspacer.explore.domain.repository.NASAImageLibrarySearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NASAImageLibrarySearchUseCase(private val nasaImageLibrarySearchRepository: NASAImageLibrarySearchRepository = NASAImageLibrarySearchImplementation()) {
    operator fun invoke(
        query: String,
        page: Int
    ): Flow<NetworkState<List<NASAImageLibrarySearchModifiedDTO>>> {
        return flow {
            try {
                emit(NetworkState.Loading(""))
                val retrievedCollectionData =
                    nasaImageLibrarySearchRepository.getResultsFromNASAImageLibrary(
                        query,
                        page
                    ).collection
                val modifiedCollectionData =
                    retrievedCollectionData.copy(items = retrievedCollectionData.items.filter { it.data.any { it.media_type == "image" } })
                emit(
                    NetworkState.Success(
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
                emit(NetworkState.Failure(e.message.toString()))
            }
        }
    }
}