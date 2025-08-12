package com.example.catlist1.leaderboard.storage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlist1.leaderboard.model.SubmitScoreRequest
import com.example.catlist1.leaderboard.repository.LeaderboardRepository
import com.example.catlist1.user.account.UserAccountStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val store: QuizResultStore,
    private val leaderboardRepository: LeaderboardRepository,
    private val userAccountStore: UserAccountStore

): ViewModel(){
   /* val results: StateFlow<List<LocalQuizResult>> = store.results.stateIn(viewModelScope, SharingStarted.Eagerly,
        emptyList()
    )*/

    val results: StateFlow<List<LocalQuizResult>> =
        store.results.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val myResults: StateFlow<List<LocalQuizResult>> =
        combine(store.results, userAccountStore.userAccount) { allResults, user ->
            val nickname = user?.nickname
            if (nickname == null) emptyList()
            else allResults.filter { it.nickname == nickname }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun publishResult(result: LocalQuizResult?) {
        viewModelScope.launch {
            if (result != null) {
                println(">>> Publishujem rezultat: $result")

                leaderboardRepository.submitScore(
                    SubmitScoreRequest(
                    nickname = result.nickname,
                    result = result.result)
                )
                store.markAsPublished(result.timestamp)
            }
        }
    }

}