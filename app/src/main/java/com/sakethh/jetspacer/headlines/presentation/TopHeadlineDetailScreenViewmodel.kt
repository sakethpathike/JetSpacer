package com.sakethh.jetspacer.headlines.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.headlines.data.repository.TopHeadlinesDataImplementation
import com.sakethh.jetspacer.headlines.domain.repository.TopHeadlinesDataRepository
import kotlinx.coroutines.launch

open class TopHeadlineDetailScreenViewmodel(
    private val topHeadlinesDataRepository: TopHeadlinesDataRepository = TopHeadlinesDataImplementation()
) : ViewModel() {
    fun bookmarkANewHeadline(id: Long) {
        viewModelScope.launch {
            topHeadlinesDataRepository.addANewHeadline(id)
        }
    }

    fun deleteAnExistingHeadlineBookmark(id: Long) {
        viewModelScope.launch {
            topHeadlinesDataRepository.deleteAHeadline(id)
        }
    }
}