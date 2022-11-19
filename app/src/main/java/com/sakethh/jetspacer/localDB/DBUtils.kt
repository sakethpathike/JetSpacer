package com.sakethh.jetspacer.localDB

import io.realm.kotlin.ext.query
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DBUtils {
    fun doesThisExistsInDBAPOD(imageURL: String): Boolean {
        DBImplementation.currentRealmObject = setOf(APOD_DB_DTO::class)
        val query = DBImplementation.realm.query<APOD_DB_DTO>("id==$0", imageURL).find().count()
        return query != 0
    }
}