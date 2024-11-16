package com.sakethh.jetspacer.common.data.local.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sakethh.jetspacer.common.data.local.domain.model.rover.RoverImage
import kotlinx.coroutines.flow.Flow

@Dao
interface RoverDAO {
    @Insert
    suspend fun addANewImage(roverImage: RoverImage)

    @Query("DELETE FROM roverimage WHERE imgUrl = :url")
    suspend fun deleteAnImage(url: String)

    @Query("SELECT COUNT(*) > 0 FROM roverimage WHERE imgUrl = :url")
    suspend fun doesThisImageExists(url: String): Boolean

    @Query("SELECT * FROM roverimage WHERE isBookmarked = 1")
    fun getAllBookmarkedImages(): Flow<List<RoverImage>>
}