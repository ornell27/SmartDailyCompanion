package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.Goal
import com.example.myapplication.data.GoalDao
import com.example.myapplication.ui.theme.BackgroundGray
import com.example.myapplication.ui.theme.PurpleMain
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(goalDao: GoalDao, onBack: () -> Unit) {
    // 1. Récupération des objectifs en temps réel
    val goals by goalDao.getAllGoals().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    // État pour le nouveau texte de l'objectif
    var newGoalTitle by remember { mutableStateOf("") }

    // --- CALCUL DE LA PROGRESSION ---
    val progress = if (goals.isNotEmpty()) {
        goals.count { it.isCompleted }.toFloat() / goals.size
    } else {
        0f
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Goals", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
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
            // --- NOUVELLE SECTION : BARRE DE PROGRESSION ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Task Completion", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            color = PurpleMain,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp),
                        color = PurpleMain,
                        trackColor = PurpleMain.copy(alpha = 0.1f),
                        strokeCap = StrokeCap.Round
                    )
                }
            }

            // --- SECTION AJOUT RAPIDE ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = newGoalTitle,
                        onValueChange = { newGoalTitle = it },
                        placeholder = { Text("Add a new goal...") },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    Button(
                        onClick = {
                            if (newGoalTitle.isNotBlank()) {
                                scope.launch {
                                    goalDao.insertGoal(Goal(title = newGoalTitle))
                                    newGoalTitle = ""
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleMain)
                    ) {
                        Text("Add")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- LISTE DES OBJECTIFS ---
            Text(
                text = "Your Tasks",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(goals) { goal ->
                    GoalItem(
                        goal = goal,
                        onToggle = { isChecked ->
                            scope.launch {
                                goalDao.insertGoal(goal.copy(isCompleted = isChecked))
                            }
                        },
                        onDelete = {
                            scope.launch { goalDao.deleteGoal(goal) }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GoalItem(goal: Goal, onToggle: (Boolean) -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = goal.isCompleted,
                onCheckedChange = onToggle,
                colors = CheckboxDefaults.colors(checkedColor = PurpleMain)
            )

            Text(
                text = goal.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = if (goal.isCompleted) Color.Gray else Color.Black
            )

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFFFB74D)
                )
            }
        }
    }
}