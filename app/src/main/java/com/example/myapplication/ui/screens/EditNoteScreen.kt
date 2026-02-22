package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.LanguageManager
import com.example.myapplication.data.Note
import com.example.myapplication.data.NoteDao
import com.example.myapplication.data.Strings
import com.example.myapplication.ui.theme.DarkBackground
import com.example.myapplication.ui.theme.DarkTextSecondary
import com.example.myapplication.ui.theme.PurpleMain
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditNoteScreen(
    noteDao: NoteDao,
    noteToEdit: Note? = null,
    isDarkMode: Boolean = false,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(noteToEdit?.title ?: "") }
    var content by remember { mutableStateOf(noteToEdit?.content ?: "") }
    val scope = rememberCoroutineScope()
    val lang by LanguageManager.currentLanguage

    // ✅ Couleurs dynamiques selon le thème
    val bgColor         = if (isDarkMode) DarkBackground       else Color(0xFFF7F8FC)
    val textColor       = if (isDarkMode) Color.White          else Color.Black
    val secondaryText   = if (isDarkMode) DarkTextSecondary    else Color.Gray
    val borderUnfocused = if (isDarkMode) Color(0xFF555555)    else Color.LightGray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)  // ✅ fond sombre
            .padding(24.dp)
    ) {
        Text(
            text = if (noteToEdit == null) Strings.newNote else Strings.editNote,
            style = MaterialTheme.typography.headlineMedium,
            color = PurpleMain,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(Strings.titleLabel, color = secondaryText) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,          // ✅
                unfocusedTextColor = textColor,        // ✅
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = PurpleMain,
                unfocusedBorderColor = borderUnfocused,
                focusedLabelColor = PurpleMain,
                unfocusedLabelColor = secondaryText,
                cursorColor = PurpleMain
            )
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text(Strings.writeHere, color = secondaryText) },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,          // ✅
                unfocusedTextColor = textColor,        // ✅
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = PurpleMain,
                unfocusedBorderColor = borderUnfocused,
                focusedLabelColor = PurpleMain,
                unfocusedLabelColor = secondaryText,
                cursorColor = PurpleMain
            )
        )

        Button(
            onClick = {
                if (title.isNotBlank()) {
                    scope.launch {
                        val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                        noteDao.insert(Note(id = noteToEdit?.id ?: 0, title = title, content = content, date = today))
                        onBack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurpleMain)
        ) {
            Text(Strings.save, color = Color.White)
        }

        TextButton(onClick = { onBack() }, modifier = Modifier.fillMaxWidth()) {
            Text(Strings.cancel, color = secondaryText)
        }
    }
}