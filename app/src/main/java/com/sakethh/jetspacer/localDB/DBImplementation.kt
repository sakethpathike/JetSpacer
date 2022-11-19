package com.sakethh.jetspacer.localDB

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabled
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DBImplementation() : DBService {
    companion object LocalDB {
        var currentRealmObject = setOf(APOD_DB_DTO::class)
        private val realmConfiguration = RealmConfiguration.create(currentRealmObject)
        val realm = Realm.open(realmConfiguration)
    }

    override suspend fun getBookMarkedAPODDBDATA(): Flow<ResultsChange<APOD_DB_DTO>> {
        currentRealmObject = setOf(APOD_DB_DTO::class)
        return realm.query<APOD_DB_DTO>("isBookMarked==$0", true).asFlow()
    }

    override suspend fun addNewBookMarkToAPODDB(apodDbDto: APOD_DB_DTO) {
        currentRealmObject = setOf(APOD_DB_DTO::class)
        realm.write {
            try {
                this.copyToRealm(apodDbDto)
                isAlertDialogEnabled.value = false
            } catch (_: java.lang.IllegalArgumentException) {
                isAlertDialogEnabled.value = true
            }
        }
    }

    override suspend fun deleteFromAPODDB(imageURL: String) {
        currentRealmObject = setOf(APOD_DB_DTO::class)
        realm.write {
            val requestedDeleteID = this.query<APOD_DB_DTO>("id==$0", imageURL).find().first()
            delete(requestedDeleteID)
        }
    }
}