package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.Note
import com.example.myapplication.data.NoteDao
import com.example.myapplication.ui.theme.PurpleMain
import kotlinx.coroutines.launch

@Composable
fun EditNoteScreen(
    noteDao: NoteDao,
    noteToEdit: Note? = null,
    onBack: () -> Unit
) {
    // On initialise avec les données existantes si noteToEdit n'est pas nul
    var title by remember { mutableStateOf(noteToEdit?.title ?: "") }
    var content by remember { mutableStateOf(noteToEdit?.content ?: "") } // Assure-toi que ta classe Note a un champ "content"

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = if (noteToEdit == null) "New Note" else "Edit Note",
            style = MaterialTheme.typography.headlineMedium,
            color = PurpleMain,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Champ pour le TITRE
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

        // Champ pour le CONTENU
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Write something...") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Prend tout l'espace disponible
                .padding(bottom = 24.dp)
        )

        // Bouton de SAUVEGARDE
        Button(
            onClick = {
                if (title.isNotBlank()) {
                    scope.launch {
                        val note = Note(
                            // Si c'est une modif, on garde l'ID original pour que Room l'écrase (Update)
                            // Si c'est nouveau (ID 0), Room crée une nouvelle entrée (Insert)
                            id = noteToEdit?.id ?: 0,
                            title = title,
                            content = content,
                            date = "18 Feb"
                        )
                        noteDao.insert(note)
                        onBack() // Retour au Dashboard
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurpleMain)
        ) {
            Text("Save Note", color = Color.White)
        }

        // Bouton ANNULER
        TextButton(
            onClick = { onBack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel", color = Color.Gray)
        }
    }
}