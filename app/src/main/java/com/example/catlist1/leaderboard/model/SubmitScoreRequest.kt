package com.example.catlist1.leaderboard.model

data class SubmitScoreRequest(
    val nickname: String,
    val result: Float,
    val category: Int = 1
)
