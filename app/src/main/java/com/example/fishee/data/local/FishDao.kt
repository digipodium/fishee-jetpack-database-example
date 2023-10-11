package com.example.fishee.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.fishee.data.local.models.Fish
import kotlinx.coroutines.flow.Flow

@Dao
interface FishDao {
    @Upsert
    suspend fun upsertFish(fish: Fish)

    @Delete
    suspend fun deleteFish(fish: Fish)

    @Query("SELECT * FROM fish WHERE user_id = :userId ORDER BY name DESC")
    fun getFishesByName(userId: Int): Flow<List<Fish>>

    @Query("SELECT * FROM fish WHERE user_id = :userId ORDER BY created_at DESC")
    fun getFishesByDate(userId: Int): Flow<List<Fish>>

    @Query("SELECT * FROM fish WHERE user_id = :userId ORDER BY weight   DESC")
    fun getFishesByWeight(userId: Int): Flow<List<Fish>>

    @Query("SELECT * FROM fish WHERE id = :id")
    fun getFishById(id: Int): Flow<Fish>

}