package com.example.myapplication.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.Mood
import com.example.myapplication.data.MoodDao
import com.example.myapplication.data.QuotesProvider
import com.example.myapplication.data.Strings
import com.example.myapplication.data.LanguageManager
import com.example.myapplication.ui.theme.BackgroundGray
import com.example.myapplication.ui.theme.DarkBackground
import com.example.myapplication.ui.theme.DarkSurface
import com.example.myapplication.ui.theme.DarkTextSecondary
import com.example.myapplication.ui.theme.PurpleMain
import com.example.myapplication.ui.theme.PurpleLight
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodScreen(
    moodDao: MoodDao,
    isDarkMode: Boolean,   // ✅ paramètre ajouté
    onBack: () -> Unit
) {
    val moods by moodDao.getAllMoods().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val lang by LanguageManager.currentLanguage

    // ✅ Couleurs dynamiques selon le thème
    val bgColor       = if (isDarkMode) DarkBackground else BackgroundGray
    val cardColor     = if (isDarkMode) DarkSurface    else Color.White
    val textColor     = if (isDarkMode) Color.White    else Color(0xFF333333)
    val secondaryText = if (isDarkMode) DarkTextSecondary else Color.Gray

    var selectedMoodEmoji by remember { mutableStateOf<String?>(null) }
    var moodQuote by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }

    val moodOptions = listOf(
        "😊" to Strings.happy,
        "😐" to Strings.neutral,
        "😔" to Strings.sad,
        "😡" to Strings.angry,
        "😴" to Strings.tired,
        "🤩" to Strings.excited,
        "😰" to Strings.anxious
    )

    val moodColors = mapOf(
        "😊" to Color(0xFFFFF176),
        "😐" to Color(0xFFB0BEC5),
        "😔" to Color(0xFF90CAF9),
        "😡" to Color(0xFFEF9A9A),
        "😴" to Color(0xFFCE93D8),
        "🤩" to Color(0xFFFFCC80),
        "😰" to Color(0xFFA5D6A7)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Strings.moodTracker, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PurpleMain,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = bgColor  // ✅ fond Scaffold sombre
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(bgColor)  // ✅
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- SECTION CHOISIR HUMEUR ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),  // ✅
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = Strings.howFeeling,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor  // ✅
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        val rows = moodOptions.chunked(4)
                        rows.forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                row.forEach { (emoji, label) ->
                                    val isSelected = selectedMoodEmoji == emoji
                                    // ✅ Couleur de fond des émojis adaptée au thème
                                    val emojiBg = if (isSelected)
                                        (moodColors[emoji] ?: PurpleLight).copy(alpha = 0.4f)
                                    else if (isDarkMode) Color(0xFF2A2A2A) else Color(0xFFF5F5F5)

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .clickable {
                                                selectedMoodEmoji = emoji
                                                moodQuote = QuotesProvider.getQuoteForMood(emoji)
                                            }
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(64.dp)
                                                .clip(CircleShape)
                                                .background(emojiBg)
                                                .then(
                                                    if (isSelected) Modifier.border(
                                                        2.dp,
                                                        moodColors[emoji] ?: PurpleMain,
                                                        CircleShape
                                                    ) else Modifier
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(emoji, fontSize = 32.sp)
                                        }
                                        Text(
                                            label,
                                            fontSize = 11.sp,
                                            color = if (isSelected) PurpleMain else secondaryText,  // ✅
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            // ✅ Citation selon l'humeur
            item {
                AnimatedVisibility(
                    visible = selectedMoodEmoji != null,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(listOf(PurpleMain, PurpleLight)),
                                RoundedCornerShape(20.dp)
                            )
                            .padding(20.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "✨",
                                fontSize = 24.sp,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = moodQuote,
                                fontSize = 15.sp,
                                fontStyle = FontStyle.Italic,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }

            // ✅ Bouton enregistrer
            item {
                AnimatedVisibility(visible = selectedMoodEmoji != null) {
                    Button(
                        onClick = {
                            selectedMoodEmoji?.let { emoji ->
                                val label = moodOptions.find { it.first == emoji }?.second ?: ""
                                val today = SimpleDateFormat(
                                    "dd MMM yyyy",
                                    Locale.getDefault()
                                ).format(Date())
                                scope.launch {
                                    moodDao.insertMood(
                                        Mood(emoji = emoji, description = label, date = today)
                                    )
                                    showSnackbar = true
                                    selectedMoodEmoji = null
                                    moodQuote = ""
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleMain)
                    ) {
                        Text(
                            Strings.moodSaved,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // ✅ Confirmation enregistrement
            item {
                AnimatedVisibility(visible = showSnackbar) {
                    LaunchedEffect(showSnackbar) {
                        kotlinx.coroutines.delay(2000)
                        showSnackbar = false
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text(
                            "✅ ${Strings.moodSaved}",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // --- HISTORIQUE ---
            item {
                Text(
                    Strings.history,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor  // ✅
                )
            }

            if (moods.isEmpty()) {
                item {
                    Text(
                        "Aucune humeur enregistrée.",
                        color = secondaryText,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                items(moods) { mood ->
                    val moodCardColor = moodColors[mood.emoji] ?: Color.White
                    // ✅ En mode sombre, fond de carte plus visible
                    val histCardColor = if (isDarkMode)
                        moodCardColor.copy(alpha = 0.25f)
                    else
                        moodCardColor.copy(alpha = 0.15f)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = histCardColor),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(
                                        moodCardColor.copy(alpha = 0.4f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(mood.emoji, fontSize = 26.sp)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    mood.description,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = textColor  // ✅
                                )
                                Text(
                                    mood.date,
                                    fontSize = 12.sp,
                                    color = secondaryText  // ✅
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}