package com.example.catlist1.user.domain

import kotlinx.serialization.Serializable

@Serializable
data class UserAccount(
    val name: String = "",
    val email: String = "",
    val nickname: String = "",
    val createdAt: Long = 0L
) {
    companion object {
        fun empty(): UserAccount = UserAccount()
    }
}
/*
data class UserAccount(
    val name: String,
    val nickname: String,
    val email: String,
    val createdAt: Long = System.currentTimeMillis()
){
    companion object {
        fun empty(): UserAccount = UserAccount(
            name = "",
            email = "",
            nickname = "",
            createdAt = 0L
        )
    }
}*/
