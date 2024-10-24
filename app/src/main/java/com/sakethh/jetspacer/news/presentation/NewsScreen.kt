package com.sakethh.jetspacer.news.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sakethh.jetspacer.news.presentation.components.NewsComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setNavigationBarColor(TopAppBarDefaults.topAppBarColors().containerColor)
    val newsScreenViewModel = viewModel<NewsScreenViewModel>()
    val topHeadlines = newsScreenViewModel.topHeadLinesState
    Scaffold(topBar = {
        Column {
            TopAppBar(actions = {

            }, title = {
                Text(
                    text = "Top Headlines",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 25.dp)
                )
            })
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.25f))
        }
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (topHeadlines.value.isLoading) {
                item {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                return@LazyColumn
            }
            if (topHeadlines.value.error) {
                item {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LinearProgressIndicator()
                    }
                }
                return@LazyColumn
            }
            items(items = topHeadlines.value.data.articles, key = { it.urlToImage }) { article ->
                NewsComponent(
                    article = article,
                    onImgClick = { -> },
                    onItemClick = { -> },
                )
            }
        }
    }
}