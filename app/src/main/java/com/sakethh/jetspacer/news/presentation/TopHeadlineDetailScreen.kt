package com.sakethh.jetspacer.news.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.news.domain.model.Article
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TopHeadlineDetailScreen(rawArticleString: String) {
    val article = rememberSaveable(saver = ArticleSaver) {
        Json.decodeFromString<Article>(rawArticleString)
    }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(article.urlToImage)
                    .crossfade(true).build(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(15.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .border(
                        1.5.dp,
                        LocalContentColor.current.copy(0.25f),
                        RoundedCornerShape(15.dp)
                    ),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp)
                    .navigationBarsPadding(),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(50.dp))
        }
    }
}

private val ArticleSaver = Saver<Article, String>(
    save = { Json.encodeToString(it) },
    restore = { Json.decodeFromString<Article>(it) }
)