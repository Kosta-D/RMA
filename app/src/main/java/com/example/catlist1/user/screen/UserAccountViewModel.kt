package com.example.catlist1.user.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlist1.user.account.UserAccountStore
import com.example.catlist1.user.domain.UserAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val userAccountStore: UserAccountStore

) : ViewModel(){
    private val _state = MutableStateFlow(UserAccountContract.UiState())
    val state = _state.asStateFlow()

    val userAccount = userAccountStore.userAccount


    private val events = MutableSharedFlow<UserAccountContract.UiEvent>()

    init {
        observeEvents()

        viewModelScope.launch {
            userAccountStore.userAccount.collect { user ->
                setState { copy(account = user) }
            }
        }
    }

    fun setEvent(event: UserAccountContract.UiEvent) = viewModelScope.launch {
        events.emit(event)
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is UserAccountContract.UiEvent.NameChanged -> {
                        setState { copy(name = event.name) }
                        validate()
                    }
                    is UserAccountContract.UiEvent.NicknameChanged -> {
                        setState { copy(nickname = event.nickname) }
                        validate()
                    }
                    is UserAccountContract.UiEvent.EmailChanged -> {
                        setState { copy(email = event.email) }
                        validate()
                    }
                    UserAccountContract.UiEvent.SaveAccount -> {
                        saveAccount()
                    }
                }
            }
        }
    }

    private fun setState(reducer: UserAccountContract.UiState.() -> UserAccountContract.UiState) =
        _state.getAndUpdate(reducer)

    private fun validate() {
        val current = state.value
        val isValid = current.name.isNotBlank() &&
                current.nickname.matches(Regex("^[A-Za-z0-9_]+\$")) &&
                current.email.matches(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"))

        setState {
            copy(
                isValid = isValid,
                errorMessage = if (isValid) null else "Unesite validne podatke."
            )
        }
    }

    private fun saveAccount() = viewModelScope.launch {
        val current = state.value
        if (!current.isValid) return@launch

        userAccountStore.replaceUserAccount(
            UserAccount(
                name = current.name,
                email = current.email,
                nickname = current.nickname,
                createdAt = System.currentTimeMillis()
            )
        )

        setState { copy(accountSaved = true) }
    }

    fun clearUserAccount() = viewModelScope.launch {
        userAccountStore.clearUserAccount()
        setState { copy(account = null, accountSaved = false) }
    }



}