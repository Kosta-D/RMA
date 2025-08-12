package com.example.catlist1.user.screen

import com.example.catlist1.user.domain.UserAccount

interface UserAccountContract {

    data class UiState(
        val name: String = "",
        val nickname: String = "",
        val email: String = "",
        val isValid: Boolean = false,
        val errorMessage: String? = null,
        val accountSaved: Boolean = false,
        val account: UserAccount?=null//novo
    )

    sealed class UiEvent {
        data class NameChanged(val name: String) : UiEvent()
        data class NicknameChanged(val nickname: String) : UiEvent()
        data class EmailChanged(val email: String) : UiEvent()
        object SaveAccount : UiEvent()
    }
}