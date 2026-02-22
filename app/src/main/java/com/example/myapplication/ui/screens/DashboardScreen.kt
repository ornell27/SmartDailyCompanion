package com.example.myapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.LanguageManager
import com.example.myapplication.data.Note
import com.example.myapplication.data.NoteDao
import com.example.myapplication.data.Strings
import com.example.myapplication.ui.components.MenuCard
import com.example.myapplication.ui.components.MotivationCard
import com.example.myapplication.ui.components.NoteRow
import com.example.myapplication.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    noteDao: NoteDao,
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    onAddNoteClick: () -> Unit,
    onEditNoteClick: (Note) -> Unit,
    onGoalsClick: () -> Unit,
    onMoodClick: () -> Unit
) {
    val notes by noteDao.getAllNotes().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // ✅ Recherche
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    // ✅ Langue
    val lang by LanguageManager.currentLanguage

    // ✅ Filtrage des notes selon la recherche
    val filteredNotes = remember(notes, searchQuery) {
        if (searchQuery.isBlank()) notes
        else notes.filter { it.title.contains(searchQuery, ignoreCase = true) || it.content.contains(searchQuery, ignoreCase = true) }
    }

    val bgColor = if (isDarkMode) DarkBackground else BackgroundGray
    val textColor = if (isDarkMode) Color.White else Color.Black
    val secondaryTextColor = if (isDarkMode) DarkTextSecondary else Color.Gray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(scrollState)
    ) {
        // --- HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isSearchVisible) 310.dp else 280.dp)
                .background(
                    brush = Brush.verticalGradient(listOf(PurpleMain, PurpleLight)),
                    shape = RoundedCornerShape(bottomStart = 45.dp, bottomEnd = 45.dp)
                )
                .padding(24.dp)
        ) {
            // Boutons en haut à droite : Langue + Thème
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ✅ Bouton langue FR / EN
                TextButton(
                    onClick = { LanguageManager.toggle() },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                ) {
                    Text(
                        text = if (LanguageManager.isFrench()) "🇫🇷 FR" else "🇬🇧 EN",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Bouton thème sombre/clair
                IconButton(onClick = onToggleTheme) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.NightsStay else Icons.Default.WbSunny,
                        contentDescription = "Toggle Theme",
                        tint = Color.White
                    )
                }

                // ✅ Bouton recherche
                IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
            }

            Column {
                Spacer(modifier = Modifier.height(50.dp))
                Text(Strings.hi, color = Color.White.copy(alpha = 0.8f), fontSize = 18.sp)
                Text(Strings.manageDay, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)

                // ✅ Barre de recherche animée
                AnimatedVisibility(visible = isSearchVisible) {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(Strings.searchHint, color = Color.White.copy(0.7f), fontSize = 14.sp) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(0.5f),
                            cursorColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                        }
                    )
                }
            }
        }

        // --- BOUTONS D'ACTION ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-40).dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MenuCard(Strings.notes, "📝", PurpleMain) { onAddNoteClick() }
            MenuCard(Strings.goals, "✅", AccentTeal) { onGoalsClick() }
            MenuCard(Strings.mood, "😊", AccentOrange) { onMoodClick() }
        }

        // --- MOTIVATION ET NOTES ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-20).dp)
        ) {
            MotivationCard()
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = Strings.recentNotes,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            // ✅ Indicateur de recherche active
            if (searchQuery.isNotBlank()) {
                Text(
                    text = "${filteredNotes.size} résultat(s) pour \"$searchQuery\"",
                    fontSize = 13.sp,
                    color = PurpleMain,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (filteredNotes.isEmpty()) {
                Text(
                    if (searchQuery.isNotBlank()) "Aucun résultat trouvé." else Strings.noNotes,
                    color = secondaryTextColor
                )
            } else {
                filteredNotes.forEach { note ->
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