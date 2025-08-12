package com.example.catlist1.leaderboard.network

import com.example.catlist1.leaderboard.model.LeaderboardEntry
import com.example.catlist1.leaderboard.model.SubmitScoreRequest
import com.example.catlist1.leaderboard.model.SubmitScoreResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LeaderboardApi {
    @GET("leaderboard")
    suspend fun getLeaderboard(@Query("category") category: Int = 1): List<LeaderboardEntry>

    @POST("leaderboard")
    suspend fun submitScore(@Body request: SubmitScoreRequest): SubmitScoreResponse
}