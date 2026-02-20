package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moods")
data class Mood(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val emoji: String,
    val description: String, // On utilise "description" ici
    val date: String
)