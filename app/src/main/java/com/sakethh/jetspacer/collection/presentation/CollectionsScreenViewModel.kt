package com.sakethh.jetspacer.collection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.collection.domain.CollectionType
import com.sakethh.jetspacer.collection.domain.model.CollectionItem
import com.sakethh.jetspacer.common.data.local.data.repository.LocalAPODImplementation
import com.sakethh.jetspacer.common.data.local.data.repository.LocalRoverImagesImplementation
import com.sakethh.jetspacer.common.data.local.domain.model.APOD
import com.sakethh.jetspacer.common.data.local.domain.model.Headline
import com.sakethh.jetspacer.common.data.local.domain.model.rover.RoverImage
import com.sakethh.jetspacer.common.data.local.domain.repository.LocalAPODRepository
import com.sakethh.jetspacer.common.data.local.domain.repository.LocalRoverImagesRepository
import com.sakethh.jetspacer.headlines.data.repository.TopHeadlinesDataImplementation
import com.sakethh.jetspacer.headlines.domain.repository.TopHeadlinesDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CollectionsScreenViewModel(
    private val topHeadlinesDataRepository: TopHeadlinesDataRepository = TopHeadlinesDataImplementation(),
    private val localAPODRepository: LocalAPODRepository = LocalAPODImplementation(),
    private val localRoverImagesRepository: LocalRoverImagesRepository = LocalRoverImagesImplementation()
) : ViewModel() {

    private val _bookMarkedTopHeadlinesData = MutableStateFlow(emptyList<Headline>())
    val bookMarkedTopHeadlinesData = _bookMarkedTopHeadlinesData.asStateFlow()

    private val _bookMarkedAPODData = MutableStateFlow(emptyList<APOD>())
    val bookMarkedAPODData = _bookMarkedAPODData.asStateFlow()

    private val _bookMarkedRoverImagesData = MutableStateFlow(emptyList<RoverImage>())
    val bookMarkedRoverImagesData = _bookMarkedRoverImagesData.asStateFlow()

    val collectionTabData = listOf(
        CollectionItem(type = CollectionType.APOD_Archive),
        CollectionItem(type = CollectionType.Mars_Gallery),
        CollectionItem(type = CollectionType.Headlines)
    )

    init {
        viewModelScope.launch {
            topHeadlinesDataRepository.getBookmarkedHeadlines().collectLatest {
                _bookMarkedTopHeadlinesData.emit(it)
            }
        }
        viewModelScope.launch {
            localAPODRepository.getAllBookmarkedAPOD().collectLatest {
                _bookMarkedAPODData.emit(it)
            }
        }
        viewModelScope.launch {
            localRoverImagesRepository.getAllBookmarkedImages().collectLatest {
                _bookMarkedRoverImagesData.emit(it)
            }
        }
    }
}