package com.sakethh.jetspacer.localDB

import android.content.Context
import android.widget.Toast
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.types.RealmObject
import kotlinx.coroutines.flow.Flow

class DBImplementation(private val dbUtils: DBUtils = DBUtils()) : DBService {


    companion object LocalDB {
        private val realmConfiguration = RealmConfiguration.create(setOf(APOD_DB_DTO::class,MarsRoversDBDTO::class))
        val realm = Realm.open(realmConfiguration)
    }


    // APOD DB
    override suspend fun getBookMarkedAPODDBDATA(): Flow<ResultsChange<APOD_DB_DTO>> {
        return realm.query<APOD_DB_DTO>("isBookMarked==$0", true).asFlow()
    }

    override suspend fun addNewBookMarkToAPODDB(apodDbDto: APOD_DB_DTO) {
        realm.write {
            try {
                this.copyToRealm(apodDbDto)
                isAlertDialogEnabledForAPODDB.value = false
            } catch (_: java.lang.IllegalArgumentException) {
                isAlertDialogEnabledForAPODDB.value = true
            }
        }

    }

    override suspend fun deleteFromAPODDB(imageURL: String) {
        realm.write {
            val requestedDeleteID = this.query<APOD_DB_DTO>("id==$0", imageURL).find().first()
            delete(requestedDeleteID)
        }
    }


    // Mars Rover DB
    override suspend fun getBookMarkedRoverDBDATA(): Flow<ResultsChange<MarsRoversDBDTO>> {
        return realm.query<MarsRoversDBDTO>("isBookMarked==$0", true).asFlow()
    }

    override suspend fun addNewBookMarkToRoverDB(
        marsRoverDbDto: MarsRoversDBDTO
    ) {
        realm.write {
            try {
                this.copyToRealm(marsRoverDbDto)
                isAlertDialogEnabledForRoversDB.value = false
            } catch (_: java.lang.IllegalArgumentException) {
                isAlertDialogEnabledForRoversDB.value = true
            }
        }


    }

    override suspend fun deleteFromRoverDB(imageURL: String) {
        realm.write {
            val requestedDeleteID = this.query<MarsRoversDBDTO>("id==$0", imageURL).find().first()
            delete(requestedDeleteID)
        }
    }
}