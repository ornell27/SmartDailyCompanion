package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.LanguageManager
import com.example.myapplication.data.Note
import com.example.myapplication.ui.screens.DashboardScreen
import com.example.myapplication.ui.screens.EditNoteScreen
import com.example.myapplication.ui.screens.GoalsScreen
import com.example.myapplication.ui.screens.MoodScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "smart_daily_db"
        ).fallbackToDestructiveMigration().build()

        val noteDao = db.noteDao()
        val goalDao = db.goalDao()
        val moodDao = db.moodDao()

        setContent {
            val systemInDark = isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf(systemInDark) }

            val currentLanguage by LanguageManager.currentLanguage

            MyApplicationTheme(darkTheme = isDarkMode) {
                var currentScreen by remember { mutableStateOf("dashboard") }
                var selectedNote by remember { mutableStateOf<Note?>(null) }

                Surface(modifier = Modifier.fillMaxSize()) {
                    when (currentScreen) {
                        "dashboard" -> DashboardScreen(
                            noteDao = noteDao,
                            isDarkMode = isDarkMode,
                            onToggleTheme = { isDarkMode = !isDarkMode },
                            onAddNoteClick = {
                                selectedNote = null
                                currentScreen = "edit"
                            },
                            onEditNoteClick = { note ->
                                selectedNote = note
                                currentScreen = "edit"
                            },
                            onGoalsClick = { currentScreen = "goals" },
                            onMoodClick = { currentScreen = "mood" }
                        )
                        "edit" -> EditNoteScreen(
                            noteDao = noteDao,
                            noteToEdit = selectedNote,
                            isDarkMode = isDarkMode,  // ✅ transmis
                            onBack = { currentScreen = "dashboard" }
                        )
                        "goals" -> GoalsScreen(
                            goalDao = goalDao,
                            isDarkMode = isDarkMode,  // ✅ transmis
                            onBack = { currentScreen = "dashboard" }
                        )
                        "mood" -> MoodScreen(
                            moodDao = moodDao,
                            isDarkMode = isDarkMode,  // ✅ transmis
                            onBack = { currentScreen = "dashboard" }
                        )
                    }
                }
            }
        }
    }
}