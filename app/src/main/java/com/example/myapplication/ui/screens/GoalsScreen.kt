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
import com.example.myapplication.data.LanguageManager
import com.example.myapplication.data.Strings
import com.example.myapplication.ui.theme.BackgroundGray
import com.example.myapplication.ui.theme.DarkBackground
import com.example.myapplication.ui.theme.DarkSurface
import com.example.myapplication.ui.theme.DarkTextSecondary
import com.example.myapplication.ui.theme.PurpleMain
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    goalDao: GoalDao,
    isDarkMode: Boolean,   // ✅ paramètre ajouté
    onBack: () -> Unit
) {
    val goals by goalDao.getAllGoals().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val lang by LanguageManager.currentLanguage

    var newGoalTitle by remember { mutableStateOf("") }

    // ✅ Couleurs dynamiques selon le thème
    val bgColor       = if (isDarkMode) DarkBackground else BackgroundGray
    val cardColor     = if (isDarkMode) DarkSurface    else Color.White
    val textColor     = if (isDarkMode) Color.White    else Color.Black
    val secondaryText = if (isDarkMode) DarkTextSecondary else Color.Gray
    val textFieldText = if (isDarkMode) Color.White    else Color.Black
    val borderColor   = if (isDarkMode) Color(0xFF555555) else Color.LightGray

    val progress = if (goals.isNotEmpty()) {
        goals.count { it.isCompleted }.toFloat() / goals.size
    } else 0f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Strings.dailyGoals, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PurpleMain,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = bgColor  // ✅ fond du Scaffold aussi en mode sombre
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(bgColor)
                .padding(20.dp)
        ) {
            // --- BARRE DE PROGRESSION ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor) // ✅
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            Strings.taskCompletion,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = textColor  // ✅
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            color = PurpleMain,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(10.dp),
                        color = PurpleMain,
                        trackColor = PurpleMain.copy(alpha = 0.2f),
                        strokeCap = StrokeCap.Round
                    )
                }
            }

            // --- AJOUT RAPIDE ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor) // ✅
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newGoalTitle,
                        onValueChange = { newGoalTitle = it },
                        placeholder = {
                            Text(Strings.addGoalHint, color = secondaryText) // ✅
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = textFieldText,        // ✅ texte visible
                            unfocusedTextColor = textFieldText,      // ✅ texte visible
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = PurpleMain,
                            unfocusedBorderColor = borderColor,      // ✅ bordure adaptée
                            cursorColor = PurpleMain
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

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
                        Text(Strings.add, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = Strings.yourTasks,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,  // ✅
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(goals) { goal ->
                    GoalItem(
                        goal = goal,
                        isDarkMode = isDarkMode,  // ✅
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
fun GoalItem(
    goal: Goal,
    isDarkMode: Boolean,  // ✅
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    val cardColor = if (isDarkMode) DarkSurface else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black
    val doneColor = if (isDarkMode) Color(0xFF888888) else Color.Gray

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor) // ✅
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = goal.isCompleted,
                onCheckedChange = onToggle,
                colors = CheckboxDefaults.colors(
                    checkedColor = PurpleMain,
                    uncheckedColor = if (isDarkMode) Color(0xFFAAAAAA) else Color.Gray  // ✅
                )
            )
            Text(
                text = goal.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = if (goal.isCompleted) doneColor else textColor  // ✅
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