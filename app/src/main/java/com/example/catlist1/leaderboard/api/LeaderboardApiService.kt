package com.example.catlist1.leaderboard.api

import com.example.catlist1.leaderboard.model.LeaderboardResult
import com.example.catlist1.leaderboard.model.SubmitScoreRequest
import com.example.catlist1.leaderboard.model.SubmitScoreResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LeaderboardApiService {
    @GET("leaderboard")
    suspend fun getLeaderboard(@Query("category") category: Int = 1): List<LeaderboardResult>

    @POST("leaderboard")
    suspend fun postResult(@Body result: SubmitScoreRequest): SubmitScoreResponse
}