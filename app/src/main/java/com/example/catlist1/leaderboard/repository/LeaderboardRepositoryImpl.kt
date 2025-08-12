package com.example.catlist1.leaderboard.repository

import com.example.catlist1.leaderboard.model.LeaderboardEntry
import com.example.catlist1.leaderboard.model.SubmitScoreRequest
import com.example.catlist1.leaderboard.model.SubmitScoreResponse
import com.example.catlist1.leaderboard.network.LeaderboardApi
import javax.inject.Inject

class LeaderboardRepositoryImpl @Inject constructor(
    private val api: LeaderboardApi
):LeaderboardRepository {
    override suspend fun getLeaderboard(): List<LeaderboardEntry> {
        return api.getLeaderboard()
    }

    override suspend fun submitScore(request: SubmitScoreRequest): SubmitScoreResponse {
        return api.submitScore(request)
    }

}