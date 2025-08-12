package com.example.catlist1.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
//import java.lang.reflect.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.example.catlist1.quiz.model.QuestionOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel = hiltViewModel(),
    onNavigateToResult: (Float, Long) -> Unit,
    onBackToHome: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    val sideEffect = viewModel.sideEffect.collectAsState(initial = null)

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(sideEffect.value) {
        when (val effect = sideEffect.value) {
            is QuizContract.SideEffect.NavigateToResult -> {
                onNavigateToResult(effect.score, effect.timestamp)
            }
            QuizContract.SideEffect.ShowCancelDialog -> {
                showDialog = true
            }
            null -> {}
        }
    }

    BackHandler {
        viewModel.setEvent(QuizContract.UiEvent.CancelQuiz)
    }

    if (showDialog) {
        CancelQuizDialog(
            onConfirm = {
                showDialog = false
                viewModel.setEvent(QuizContract.UiEvent.FinishQuiz)
                onBackToHome()
            },
            onDismiss = {
                showDialog = false
            }
        )
    }

    val currentQuestion = state.value.questions.getOrNull(state.value.currentQuestionIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kviz: ${(state.value.currentQuestionIndex + 1)}/${state.value.questions.size}",
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    Text(
                        text = "⏳ ${state.value.timeRemaining}s",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
        }
    ) { padding ->
        when {
            state.value.loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.value.quizFinished -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Kviz završen!", style = MaterialTheme.typography.headlineMedium)
                }
            }
            currentQuestion != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SubcomposeAsyncImage(
                        model = currentQuestion.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )

                    Text(
                        text = currentQuestion.text,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        currentQuestion.options.forEach { option: QuestionOption ->
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    viewModel.setEvent(
                                        QuizContract.UiEvent.AnswerQuestion(
                                            questionId = currentQuestion.id,
                                            answerId = option.id
                                        )
                                    )
                                    viewModel.setEvent(QuizContract.UiEvent.NextQuestion)
                                }
                            ) {
                                Text(option.text)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            viewModel.setEvent(QuizContract.UiEvent.CancelQuiz)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Prekini kviz")
                    }
                }
            }
        }
    }
}

@Composable
fun CancelQuizDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Prekini kviz") },
        text = { Text("Da li ste sigurni da želite da prekinete kviz? Rezultat se neće računati.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Prekini")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Otkaži")
            }
        }
    )
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel = hiltViewModel(),
    onNavigateToResult: (Float,Long) -> Unit,
    onBackToHome: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    val sideEffect = viewModel.sideEffect.collectAsState(initial = null)

    // SideEffect handler (Navigate to Result ili Show Cancel dialog)
    sideEffect.value?.let { effect ->
        when (effect) {
            is QuizContract.SideEffect.NavigateToResult -> {
               onNavigateToResult(effect.score,effect.timestamp)
            }
            QuizContract.SideEffect.ShowCancelDialog -> {
                CancelQuizDialog(
                    onConfirm = { viewModel.setEvent(QuizContract.UiEvent.FinishQuiz) },
                    onDismiss = { /* ništa, dialog se skloni sam */ }
                )
            }
        }
    }

    // Back dugme → ponašaj se kao Cancel
    BackHandler {
        viewModel.setEvent(QuizContract.UiEvent.CancelQuiz)
    }

    val currentQuestion = state.value.questions.getOrNull(state.value.currentQuestionIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kviz: ${(state.value.currentQuestionIndex + 1)}/${state.value.questions.size}",
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    Text(
                        text = "⏳ ${state.value.timeRemaining}s",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
        }
    ) { padding ->
        if (state.value.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.value.quizFinished) {
            // Kad završi, možeš prikazati poruku ili odmah navigirati ka QuizResultScreen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Kviz završen!", style = MaterialTheme.typography.headlineMedium)
            }
        } else if (currentQuestion != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SubcomposeAsyncImage(
                    model = currentQuestion.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )

                Text(
                    text = currentQuestion.text,
                    style = MaterialTheme.typography.titleMedium
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    currentQuestion.options.forEach { option: QuestionOption ->
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.setEvent(
                                    QuizContract.UiEvent.AnswerQuestion(
                                        questionId = currentQuestion.id,
                                        answerId = option.id
                                    )
                                )
                                viewModel.setEvent(QuizContract.UiEvent.NextQuestion)
                            }
                        ) {
                            Text(option.text)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { viewModel.setEvent(QuizContract.UiEvent.CancelQuiz) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Prekini kviz")
                }
            }
        }
    }
}

@Composable
fun CancelQuizDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Prekini kviz") },
        text = { Text("Da li ste sigurni da želite da prekinete kviz? Rezultat se neće računati.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Prekini")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Otkaži")
            }
        }
    )
}*/