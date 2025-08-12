package com.example.catlist1.leaderboard.storage

import kotlinx.serialization.Serializable


@Serializable
data class LocalQuizResult(
    val nickname: String,
    val result: Float,
    val timestamp: Long,
    val published: Boolean
)
