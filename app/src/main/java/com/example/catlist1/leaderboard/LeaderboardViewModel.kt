package com.example.catlist1.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlist1.leaderboard.model.LeaderboardEntry
import com.example.catlist1.leaderboard.model.SubmitScoreRequest
import com.example.catlist1.leaderboard.repository.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository
): ViewModel() {

    private val _leaderboard = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val leaderboard: StateFlow<List<LeaderboardEntry>> = _leaderboard.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun loadLeaderboard() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = repository.getLeaderboard()
                _leaderboard.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    fun submitScore(nickname: String, result: Float, onSuccess: (Int) -> Unit) {//viska
        viewModelScope.launch {
            try {
                val response = repository.submitScore(
                    SubmitScoreRequest(
                        nickname = nickname,
                        result = result.coerceIn(0f, 100f)
                    )
                )
                onSuccess(response.ranking)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}