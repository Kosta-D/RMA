package com.example.catlist1.leaderboard.model

import com.example.catlist1.leaderboard.model.LeaderboardEntry

data class SubmitScoreResponse(
    val result: LeaderboardEntry,
    val ranking: Int
)
