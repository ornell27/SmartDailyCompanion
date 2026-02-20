package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.Mood
import com.example.myapplication.data.MoodDao
import com.example.myapplication.ui.theme.BackgroundGray
import com.example.myapplication.ui.theme.PurpleMain
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodScreen(moodDao: MoodDao, onBack: () -> Unit) {
    val moods by moodDao.getAllMoods().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    // Liste des options d'humeur
    val moodOptions = listOf(
        "😊" to "Happy",
        "😐" to "Neutral",
        "😔" to "Sad",
        "😡" to "Angry",
        "😴" to "Tired"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mood Tracker", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundGray)
                .padding(20.dp)
        ) {

            Text("How are you feeling today?", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(20.dp))

            // Ligne d'émojis cliquables
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                moodOptions.forEach { (emoji, desc) -> // On utilise 'desc' pour la clarté
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.White, CircleShape)
                                .clickable {
                                    scope.launch {
                                        // MODIFIÉ : Utilise 'description' au lieu de 'label'
                                        moodDao.insertMood(Mood(emoji = emoji, description = desc, date = "18 Feb"))
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(emoji, fontSize = 30.sp)
                        }
                        Text(desc, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Text("History", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            // Historique des humeurs
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(moods) { mood ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(mood.emoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                // MODIFIÉ : Utilise mood.description au lieu de mood.label
                                Text(mood.description, fontWeight = FontWeight.Bold)
                                Text(mood.date, fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}