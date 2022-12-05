package com.sakethh.jetspacer.localDB

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class APOD_DB_DTO : RealmObject{
    var title: String = ""
    var datePublished: String = ""
    var description: String = ""
    var imageURL: String = ""
    var mediaType:String=""
    var isBookMarked: Boolean = false

    @PrimaryKey
    var id: String = ""
}