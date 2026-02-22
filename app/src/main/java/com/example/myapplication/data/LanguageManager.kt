package com.example.myapplication.data

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf

// ✅ Langue active globale — "fr" ou "en"
object LanguageManager {
    val currentLanguage = mutableStateOf("fr")

    fun toggle() {
        currentLanguage.value = if (currentLanguage.value == "fr") "en" else "fr"
    }

    fun isFrench() = currentLanguage.value == "fr"
}

// ✅ Toutes les chaînes de l'application dans les deux langues
object Strings {
    // --- DASHBOARD ---
    val hi get() = if (LanguageManager.isFrench()) "Bonjour," else "Hi,"
    val manageDay get() = if (LanguageManager.isFrench()) "Gérez votre journée" else "Manage your day"
    val notes get() = if (LanguageManager.isFrench()) "Notes" else "Notes"
    val goals get() = if (LanguageManager.isFrench()) "Objectifs" else "Goals"
    val mood get() = if (LanguageManager.isFrench()) "Humeur" else "Mood"
    val recentNotes get() = if (LanguageManager.isFrench()) "Notes récentes" else "Recent Notes"
    val noNotes get() = if (LanguageManager.isFrench()) "Aucune note pour l'instant." else "No notes yet."
    val searchHint get() = if (LanguageManager.isFrench()) "Rechercher une note..." else "Search a note..."
    val newQuote get() = if (LanguageManager.isFrench()) "Nouvelle citation" else "New quote"

    // --- EDIT NOTE ---
    val newNote get() = if (LanguageManager.isFrench()) "Nouvelle note" else "New Note"
    val editNote get() = if (LanguageManager.isFrench()) "Modifier la note" else "Edit Note"
    val titleLabel get() = if (LanguageManager.isFrench()) "Titre" else "Title"
    val writeHere get() = if (LanguageManager.isFrench()) "Écrivez quelque chose..." else "Write something..."
    val save get() = if (LanguageManager.isFrench()) "Sauvegarder" else "Save Note"
    val cancel get() = if (LanguageManager.isFrench()) "Annuler" else "Cancel"

    // --- GOALS ---
    val dailyGoals get() = if (LanguageManager.isFrench()) "Objectifs du jour" else "Daily Goals"
    val taskCompletion get() = if (LanguageManager.isFrench()) "Progression" else "Task Completion"
    val addGoalHint get() = if (LanguageManager.isFrench()) "Ajouter un objectif..." else "Add a new goal..."
    val add get() = if (LanguageManager.isFrench()) "Ajouter" else "Add"
    val yourTasks get() = if (LanguageManager.isFrench()) "Vos tâches" else "Your Tasks"

    // --- MOOD ---
    val moodTracker get() = if (LanguageManager.isFrench()) "Suivi d'humeur" else "Mood Tracker"
    val howFeeling get() = if (LanguageManager.isFrench()) "Comment vous sentez-vous aujourd'hui ?" else "How are you feeling today?"
    val history get() = if (LanguageManager.isFrench()) "Historique" else "History"
    val moodSaved get() = if (LanguageManager.isFrench()) "Humeur enregistrée !" else "Mood saved!"

    // --- HUMEURS ---
    val happy get() = if (LanguageManager.isFrench()) "Heureux" else "Happy"
    val neutral get() = if (LanguageManager.isFrench()) "Neutre" else "Neutral"
    val sad get() = if (LanguageManager.isFrench()) "Triste" else "Sad"
    val angry get() = if (LanguageManager.isFrench()) "En colère" else "Angry"
    val tired get() = if (LanguageManager.isFrench()) "Fatigué" else "Tired"
    val excited get() = if (LanguageManager.isFrench()) "Enthousiaste" else "Excited"
    val anxious get() = if (LanguageManager.isFrench()) "Anxieux" else "Anxious"
}