package com.example.catlist1.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlist1.leaderboard.storage.QuizResultStore
import com.example.catlist1.user.account.UserAccountStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userAccountStore: UserAccountStore,
    private val quizResultStore: QuizResultStore
) : ViewModel(){

    val filteredResults = combine(
        userAccountStore.userAccount,
        quizResultStore.results
    ) { user, allResults ->
        val nickname = user?.nickname
        if (nickname == null) emptyList()
        else allResults.filter { it.nickname == nickname }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

}