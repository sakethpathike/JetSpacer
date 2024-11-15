package com.sakethh.jetspacer.collection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.collection.domain.CollectionType
import com.sakethh.jetspacer.collection.domain.model.CollectionItem
import com.sakethh.jetspacer.common.data.local.domain.model.Headline
import com.sakethh.jetspacer.headlines.data.repository.TopHeadlinesDataImplementation
import com.sakethh.jetspacer.headlines.domain.repository.TopHeadlinesDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CollectionsScreenViewModel(
    private val topHeadlinesDataRepository: TopHeadlinesDataRepository = TopHeadlinesDataImplementation()
) : ViewModel() {

    private val _bookMarkedTopHeadlinesData = MutableStateFlow(emptyList<Headline>())
    val bookMarkedTopHeadlinesData = _bookMarkedTopHeadlinesData.asStateFlow()

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
    }
}