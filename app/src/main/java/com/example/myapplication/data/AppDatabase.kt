package com.example.myapplication.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// 1. La structure de la note
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String, // Nouveau : pour le texte de la note
    val date: String
)

// 2. Les actions possibles
@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Insert
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note) // AJOUTE CETTE LIGNE
}

// 3. La base de données
// Change version à 3
@Database(entities = [Note::class, Goal::class, Mood::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun goalDao(): GoalDao
    abstract fun moodDao(): MoodDao // Ajoute ceci
}