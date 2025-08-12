package com.example.catlist1.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResultScreen(
    score: Float,
    timestamp: Long,
    isPublished: Boolean,
    onBackToHome:() -> Unit,
    onViewLeaderboard: () -> Unit,
    onPublish: () -> Unit,
    //isPublished: Boolean
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rezultat kviza") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (!isPublished) {
                Button(onClick = onPublish) {
                    Text("Objavi rezultat")
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = "Tvoj rezultat:",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${score.toInt()} / 100",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = onBackToHome) {
                Text("Vrati se na početnu")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onViewLeaderboard){
                Text("Pogledaj leaderboard")
            }





        }
    }
}