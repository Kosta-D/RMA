package com.example.catlist1.quiz.model

data class Question(
    val id: String,
    val type: QuestionType,
    val imageUrl: String,
    val text: String,
    val options: List<QuestionOption>,
    val correctAnswerId: String
)
