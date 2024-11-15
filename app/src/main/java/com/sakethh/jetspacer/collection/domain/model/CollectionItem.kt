package com.sakethh.jetspacer.collection.domain.model

import com.sakethh.jetspacer.collection.domain.CollectionType

data class CollectionItem(
    val type: CollectionType,
    val name: String = type.name.replace("_", " "),
)
