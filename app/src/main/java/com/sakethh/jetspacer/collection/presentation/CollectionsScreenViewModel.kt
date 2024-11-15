package com.sakethh.jetspacer.collection.presentation

import androidx.lifecycle.ViewModel
import com.sakethh.jetspacer.collection.domain.CollectionType
import com.sakethh.jetspacer.collection.domain.model.CollectionItem

class CollectionsScreenViewModel : ViewModel() {
    val collectionTabData = listOf(
        CollectionItem(type = CollectionType.APOD_Archive),
        CollectionItem(type = CollectionType.Mars_Gallery),
        CollectionItem(type = CollectionType.News)
    )
}