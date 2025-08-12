package com.example.catlist1.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    isAccountMissing: Boolean,
    onNavigateToAccount: () -> Unit,
    onBreedsClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onProfileClick: () -> Unit,
    onResetProfile: () -> Unit // za etstiranje

) {


    LaunchedEffect(isAccountMissing) {
        if (isAccountMissing) {
            onNavigateToAccount()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "🏠 Home")

        Button(onClick = onBreedsClick) {
            Text(text = "Breeds")
        }

        Button(onClick = onQuizClick) {
            Text(text = "Quiz")
        }

        Button(onClick = onLeaderboardClick) {
            Text(text = "Leaderboard")
        }

        Button(onClick = onProfileClick) {
            Text(text = "Profile")
        }

        Button(onClick = onResetProfile) {
            Text("Poništi profil")
        }



    }
}
