package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.Note
import com.example.myapplication.data.NoteDao
import com.example.myapplication.ui.components.MenuCard
import com.example.myapplication.ui.components.NoteRow
import com.example.myapplication.ui.components.MotivationCard
import com.example.myapplication.ui.theme.*
import kotlinx.coroutines.launch

// --- LES BONS IMPORTS POUR LE SOLEIL ET LA LUNE ---
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.NightsStay

@Composable
fun DashboardScreen(
    noteDao: NoteDao,
    onAddNoteClick: () -> Unit,
    onEditNoteClick: (Note) -> Unit,
    onGoalsClick: () -> Unit,
    onMoodClick: () -> Unit
) {
    val notes by noteDao.getAllNotes().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // --- ÉTAT DU THÈME ---
    val systemInDark = isSystemInDarkTheme()
    var isDarkMode by remember { mutableStateOf(systemInDark) }

    // --- COULEURS DYNAMIQUES ---
    val bgColor = if (isDarkMode) DarkBackground else BackgroundGray
    val textColor = if (isDarkMode) Color.White else Color.Black
    val secondaryTextColor = if (isDarkMode) DarkTextSecondary else Color.Gray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(scrollState)
    ) {
        // --- 1. HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    brush = Brush.verticalGradient(listOf(PurpleMain, PurpleLight)),
                    shape = RoundedCornerShape(bottomStart = 45.dp, bottomEnd = 45.dp)
                )
                .padding(24.dp)
        ) {
            // BOUTON SWITCH THÈME
            IconButton(
                onClick = { isDarkMode = !isDarkMode },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 32.dp)
            ) {
                Icon(
                    // LUNE si on est en mode sombre, SOLEIL si on est en mode clair
                    imageVector = if (isDarkMode) Icons.Default.NightsStay else Icons.Default.WbSunny,
                    contentDescription = "Toggle Theme",
                    tint = Color.White
                )
            }

            Column {
                Spacer(modifier = Modifier.height(50.dp))
                Text("Hi Rakib,", color = Color.White.copy(alpha = 0.8f), fontSize = 18.sp)
                Text("Manage your day", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        }

        // --- 2. BOUTONS D'ACTION ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-40).dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MenuCard("Notes", "📝", PurpleMain) { onAddNoteClick() }
            MenuCard("Goals", "✅", AccentTeal) { onGoalsClick() }
            MenuCard("Mood", "😊", AccentOrange) { onMoodClick() }
        }

        // --- 3. MOTIVATION ET NOTES ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-20).dp)
        ) {
            MotivationCard()
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Recent Notes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (notes.isEmpty()) {
                Text("No notes yet.", color = secondaryTextColor)
            } else {
                notes.forEach { note ->
                    NoteRow(
                        title = note.title,
                        date = note.date,
                        onDoubleTap = { onEditNoteClick(note) },
                        onLongClick = { scope.launch { noteDao.delete(note) } }
                    )
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}