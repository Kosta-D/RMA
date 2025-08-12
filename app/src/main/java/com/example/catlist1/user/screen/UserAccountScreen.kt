package com.example.catlist1.user.screen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.catlist1.leaderboard.storage.LocalQuizResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAccountScreen(
    state: UserAccountContract.UiState,
    eventPublisher: (UserAccountContract.UiEvent) -> Unit,
    onAccountSaved: () -> Unit,
    //n
    results: List<LocalQuizResult> = emptyList()
) {

    LaunchedEffect(state.accountSaved) {
        if (state.accountSaved) {
            onAccountSaved()
        }
    }
    LaunchedEffect(results) {

        println(">>> Rezultati u profilu: $results")
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.account == null || state.account.nickname.isBlank()) {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { eventPublisher(UserAccountContract.UiEvent.NameChanged(it)) },
                    label = { Text("Ime i prezime") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.nickname,
                    onValueChange = { eventPublisher(UserAccountContract.UiEvent.NicknameChanged(it)) },
                    label = { Text("Nickname") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { eventPublisher(UserAccountContract.UiEvent.EmailChanged(it)) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                state.errorMessage?.let { errorMsg ->
                    Text(
                        text = errorMsg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { eventPublisher(UserAccountContract.UiEvent.SaveAccount) },
                    enabled = state.isValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sačuvaj Nalog")
                }
            } else {
                // Prikaz profila (za sada samo osnovno)
                Text("Ime: ${state.account.name}")
                Text("Email: ${state.account.email}")
                Text("Nadimak: ${state.account.nickname}")

                Spacer(modifier = Modifier.height(24.dp))

                Text("Ukupno kvizova: ${results.size}", style = MaterialTheme.typography.titleMedium)

                val bestScore = results.maxByOrNull { it.result }
                bestScore?.let {
                    Text("Najbolji rezultat: ${it.result.toInt()} pts")
                    Text("Status: ${if (it.published) "Objavljen ✅" else "Lokalni ❌"}")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Istorija kviz rezultata:", style = MaterialTheme.typography.titleMedium)

                if (results.isEmpty()) {
                    Text("Nema sačuvanih rezultata.")
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        results.sortedByDescending { it.timestamp }.forEach {
                            Text(
                                text = "- ${it.result.toInt()} pts — ${if (it.published) "Objavljeno ✅" else "Lokalno ❌"}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }

    /*
    LaunchedEffect(state.accountSaved) {
        if (state.accountSaved) {
            onAccountSaved()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kreiraj Nalog") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { eventPublisher(UserAccountContract.UiEvent.NameChanged(it)) },
                label = { Text("Ime i prezime") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.nickname,
                onValueChange = { eventPublisher(UserAccountContract.UiEvent.NicknameChanged(it)) },
                label = { Text("Nickname") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = { eventPublisher(UserAccountContract.UiEvent.EmailChanged(it)) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            state.errorMessage?.let { errorMsg ->
                Text(
                    text = errorMsg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { eventPublisher(UserAccountContract.UiEvent.SaveAccount) },
                enabled = state.isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sačuvaj Nalog")
            }

            //n

            Spacer(modifier = Modifier.height(32.dp))


            Spacer(modifier = Modifier.height(32.dp))

            Text("Istorija kviz rezultata:", style = MaterialTheme.typography.titleMedium)

            results.forEach {
                Text("- ${it.result.toInt()} pts, ${if (it.published) "objavljeno" else "nije objavljeno"}")
            }

            /*
            Text("Istorija rezultata:", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            if (results.isEmpty()) {
                Text("Nema sačuvanih rezultata.")
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    results.sortedByDescending { it.timestamp }.forEach { result ->
                        Text(
                            text = "${result.result.toInt()} / 100 (${if (result.published) "Objavljen" else "Lokalni"})",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

             */

        }
    }

     */
}