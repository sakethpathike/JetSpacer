package com.sakethh.jetspacer.explore.domain.useCase

import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.explore.data.repository.NASAImageLibrarySearchImplementation
import com.sakethh.jetspacer.explore.domain.model.NASAImageLibrarySearchDTO
import com.sakethh.jetspacer.explore.domain.repository.NASAImageLibrarySearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NASAImageLibrarySearchUseCase(private val nasaImageLibrarySearchRepository: NASAImageLibrarySearchRepository = NASAImageLibrarySearchImplementation()) {
    operator fun invoke(query: String, page: Int): Flow<NetworkState<NASAImageLibrarySearchDTO>> {
        return flow {
            try {
                emit(NetworkState.Loading(""))
                emit(
                    NetworkState.Success(
                        nasaImageLibrarySearchRepository.getResultsFromNASAImageLibrary(
                            query,
                            page
                        )
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                emit(NetworkState.Failure(e.message.toString()))
            }
        }
    }
}