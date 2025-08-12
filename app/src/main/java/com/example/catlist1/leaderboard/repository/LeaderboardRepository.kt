package com.example.catlist1.leaderboard.repository

import com.example.catlist1.leaderboard.model.SubmitScoreResponse
import com.example.catlist1.leaderboard.model.LeaderboardEntry
import com.example.catlist1.leaderboard.model.SubmitScoreRequest

interface LeaderboardRepository {
    suspend fun getLeaderboard(): List<LeaderboardEntry>
    suspend fun submitScore(request: SubmitScoreRequest): SubmitScoreResponse
}