package com.sakethh.jetspacer.localDB

import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DBUtils {
    fun doesThisExistsInDBAPOD(imageURL: String): Boolean {
        val query = DBImplementation.realm.query<APOD_DB_DTO>("id==$0", imageURL).find().count()
        return query != 0

    }fun doesThisExistsInDBRover(imageURL: String): Boolean {
        val query = DBImplementation.realm.query<MarsRoversDBDTO>("id==$0", imageURL).find().count()
        return query != 0
    }
}