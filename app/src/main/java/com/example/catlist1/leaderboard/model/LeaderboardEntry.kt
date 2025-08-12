package com.example.catlist1.leaderboard.model

data class LeaderboardEntry(
    val nickname: String,
    val result: Float,
    val category: Int,
    val createdAt: Long
)
