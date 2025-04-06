package com.sakethh.jetspacer.domain

data class CollectionItem(
    val type: CollectionType,
    val name: String = type.name.replace("_", " "),
)