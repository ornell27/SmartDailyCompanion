package com.example.myapplication.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Query("SELECT * FROM moods ORDER BY id DESC")
    fun getAllMoods(): Flow<List<Mood>>

    @Insert
    suspend fun insertMood(mood: Mood)
}