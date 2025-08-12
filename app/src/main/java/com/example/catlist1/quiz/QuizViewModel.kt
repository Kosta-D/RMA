package com.example.catlist1.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlist1.leaderboard.storage.LocalQuizResult
import com.example.catlist1.leaderboard.storage.QuizResultStore
import com.example.catlist1.quiz.repository.QuizRepository
import com.example.catlist1.user.account.UserAccountStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import perfetto.protos.UiState
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository,
    private val resultStore: QuizResultStore,
    private val userAccountStore: UserAccountStore
) : ViewModel(){
    private val _state = MutableStateFlow(QuizContract.UiState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuizContract.SideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private var timerJob: Job? = null

    init {
        startQuiz()
    }

    private fun startQuiz() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }

            val questions = repository.generateQuiz()

            _state.update {
                it.copy(
                    loading = false,
                    questions = questions,
                    currentQuestionIndex = 0,
                    selectedAnswers = emptyMap(),
                    quizFinished = false,
                    timeRemaining = 300L
                )
            }

            startTimer()
        }
    }

    fun setEvent(event: QuizContract.UiEvent) {
        when (event) {
            is QuizContract.UiEvent.AnswerQuestion -> answerQuestion(event.questionId, event.answerId)
            QuizContract.UiEvent.NextQuestion -> goToNextQuestion()
            QuizContract.UiEvent.FinishQuiz -> finishQuiz()
            //QuizContract.UiEvent.CancelQuiz -> showCancelDialog()
            QuizContract.UiEvent.CancelQuiz -> cancelQuiz()
            QuizContract.UiEvent.TimeTick -> updateTimer()

        }
    }

    private fun cancelQuiz() {//fix
        _state.update { it.copy(wasCancelled = true) }
        showCancelDialog()
    }



    private fun answerQuestion(questionId: String, answerId: String) {
        _state.update {
            it.copy(
                selectedAnswers = it.selectedAnswers + (questionId to answerId)
            )
        }
    }

    private fun goToNextQuestion() {
        _state.update {
            val nextIndex = it.currentQuestionIndex + 1
            if (nextIndex >= it.questions.size) {
                it.copy(quizFinished = true)
            } else {
                it.copy(currentQuestionIndex = nextIndex)
            }
        }

        if (_state.value.quizFinished) {
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        stopTimer()
        //fix
        val currentState = _state.value

        if (currentState.wasCancelled) {
            return
        }
        //

        val score = calculateScore(
            selectedAnswers = _state.value.selectedAnswers,
            questions = _state.value.questions,
            timeRemaining = _state.value.timeRemaining
        )



        viewModelScope.launch {
            val timestamp = System.currentTimeMillis()
            val nickname = userAccountStore.userAccount.value?.nickname ?: "anonymous"


            val result = LocalQuizResult(
                nickname = nickname,
                result = score,
                timestamp = System.currentTimeMillis(),
                published = false
            )
            println(">>> Generisan rezultat: $result")


            resultStore.saveResult(result)
            resultStore.debugPrintAll()

            _sideEffect.emit(QuizContract.SideEffect.NavigateToResult(score, timestamp))
        }
    }

    private fun showCancelDialog() {
        viewModelScope.launch {
            _sideEffect.emit(QuizContract.SideEffect.ShowCancelDialog)
        }
    }

    private fun updateTimer() {
        val newTime = (_state.value.timeRemaining - 1).coerceAtLeast(0L)

        _state.update {
            it.copy(timeRemaining = newTime)
        }

        if (newTime == 0L) {
            finishQuiz()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (_state.value.timeRemaining > 0L) {
                delay(1000L)
                setEvent(QuizContract.UiEvent.TimeTick)
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    private fun calculateScore(
        selectedAnswers: Map<String, String>,
        questions: List<com.example.catlist1.quiz.model.Question>,
        timeRemaining: Long
    ): Float {
        val BTO = questions.count { q ->
            val userAnswer = selectedAnswers[q.id]
            userAnswer == q.correctAnswerId
        }

        val MVT = 300f
        val PVT = timeRemaining.toFloat()
        val UBP = BTO * 2.5f * (1f + (PVT + 120f) / MVT)

        return UBP.coerceAtMost(100f)
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }

}