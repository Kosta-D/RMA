package com.example.catlist1.quiz

import com.example.catlist1.quiz.model.Question

interface QuizContract {
    data class UiState(
        val loading: Boolean = true,
        val questions: List<Question> = emptyList(),
        val currentQuestionIndex: Int = 0,
        val selectedAnswers: Map<String, String> = emptyMap(),
        val quizFinished: Boolean = false,
        val timeRemaining: Long = 300L, // sekundi
        val wasCancelled: Boolean = false//fix
    )

    sealed class UiEvent {
        data class AnswerQuestion(val questionId: String, val answerId: String) : UiEvent()
        object NextQuestion : UiEvent()
        object FinishQuiz : UiEvent()
        object CancelQuiz : UiEvent()
        object TimeTick : UiEvent()
    }

    sealed class SideEffect {
        object ShowCancelDialog : SideEffect()
        data class NavigateToResult(val score: Float,val timestamp: Long) : SideEffect()
    }
}