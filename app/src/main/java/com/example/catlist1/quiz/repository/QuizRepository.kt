package com.example.catlist1.quiz.repository

import com.example.catlist1.quiz.model.Question

interface QuizRepository {
    suspend fun generateQuiz(): List<Question>
}