package com.example.catlist1.leaderboard.model

data class LeaderboardResult(
    val nickname: String,
    val result: Float,
    val category: Int,
    val createdAt: Long
)
