package com.example.catlist1.leaderboard.storage

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.quizDataStore by preferencesDataStore(name = "quiz_results")

@Singleton
class QuizResultStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val RESULTS_KEY = stringPreferencesKey("quiz_results")
    private val scope = CoroutineScope(Dispatchers.IO)


    val results: Flow<List<LocalQuizResult>> = context.quizDataStore.data
        .map { prefs ->
            val jsonString = prefs[RESULTS_KEY]
            if (jsonString != null) {
                try {
                    Json.decodeFromString<List<LocalQuizResult>>(jsonString)
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }

    //old
    /*val results: StateFlow<List<LocalQuizResult>> = context.quizDataStore.data
        .map { prefs ->
            val jsonString = prefs[RESULTS_KEY]
            if (jsonString != null) {
                try {
                    Json.decodeFromString<List<LocalQuizResult>>(jsonString)
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = runBlocking { loadAllResults() }
        )*/

    suspend fun saveResult(result: LocalQuizResult) {
        val current = loadAllResults().toMutableList()
        current.add(result)

        println(">>> Čuvam rezultat: $result")  // 🔍 DODAJ OVO


        val json = Json.encodeToString<List<LocalQuizResult>>(current)
        context.quizDataStore.edit { prefs ->
            prefs[RESULTS_KEY] = json
        }
    }

    suspend fun debugPrintAll() {
        val current = loadAllResults()
        println(">>> DEBUG: Local results from file = $current")
    }


    suspend fun markAsPublished(timestamp: Long) {
        val updated = loadAllResults().map {
            if (it.timestamp == timestamp){
                it.copy(published = true)
            } else it
        }

        val json = Json.encodeToString<List<LocalQuizResult>>(updated)
        context.quizDataStore.edit { prefs ->
            prefs[RESULTS_KEY] = json
        }
    }

    private suspend fun loadAllResults(): List<LocalQuizResult> {
        val prefs = context.quizDataStore.data.first()
        val json = prefs[RESULTS_KEY]
        return if (json != null) {
            try {
                Json.decodeFromString<List<LocalQuizResult>>(json)
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }
}
