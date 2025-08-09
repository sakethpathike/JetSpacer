package com.sakethh.jetspacer.ui.screens.headlines

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.data.repository.TopHeadlineCacheImplementation
import com.sakethh.jetspacer.data.repository.TopHeadlinesDataImplementation
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.repository.TopHeadlinesCacheRepository
import com.sakethh.jetspacer.domain.repository.TopHeadlinesDataRepository
import com.sakethh.jetspacer.domain.useCase.FetchRemoteTopHeadlinesUseCase
import com.sakethh.jetspacer.ui.utils.uiEvent.UIEvent
import com.sakethh.jetspacer.ui.utils.uiEvent.UiChannel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TopHeadlinesScreenViewModel(
    private val topHeadlinesDataRepository: TopHeadlinesDataRepository = TopHeadlinesDataImplementation(),
    private val topHeadlinesCacheRepository: TopHeadlinesCacheRepository = TopHeadlineCacheImplementation()
) : TopHeadlineDetailScreenViewmodel() {

    init {
    }
}