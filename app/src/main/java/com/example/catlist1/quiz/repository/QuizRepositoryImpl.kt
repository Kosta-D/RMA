package com.example.catlist1.quiz.repository

import com.example.catlist1.breeds.domain.Breed
import com.example.catlist1.breeds.domain.BreedRepository
import com.example.catlist1.db.BreedPhoto
import com.example.catlist1.quiz.model.Question
import com.example.catlist1.quiz.model.QuestionOption
import com.example.catlist1.quiz.model.QuestionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

class QuizRepositoryImpl @Inject constructor(
    private val breedRepository: BreedRepository
): QuizRepository {

    override suspend fun generateQuiz(): List<Question> = withContext(Dispatchers.Default) {
        val breeds = breedRepository.getAllBreeds()
        val allBreedPhotos = breeds.flatMap { breed ->
            breedRepository.observeBreedPhotos(breed.id).firstOrNull() ?: emptyList()
        }.shuffled()

        val usedPhotos = mutableSetOf<String>()

        val questions = mutableListOf<Question>()

        repeat(20) {
            val type = if (Random.nextBoolean()) QuestionType.GUESS_THE_FACT else QuestionType.ODD_ONE_OUT

            val question = when (type) {
                QuestionType.GUESS_THE_FACT -> generateGuessTheFactQuestion(breeds, allBreedPhotos, usedPhotos)
                QuestionType.ODD_ONE_OUT -> generateOddOneOutQuestion(breeds, allBreedPhotos, usedPhotos)
            }

            if (question != null) {
                questions.add(question)
            }
        }

        questions
    }

    private fun generateGuessTheFactQuestion(
        breeds: List<Breed>,
        allBreedPhotos: List<BreedPhoto>,
        usedPhotos: MutableSet<String>
    ): Question? {
        val availablePhotos = allBreedPhotos.filter { it.photoId !in usedPhotos }
        if (availablePhotos.isEmpty()) return null

        val photo = availablePhotos.random()
        usedPhotos.add(photo.photoId)

        val correctBreed = breeds.find { it.id == photo.breedId } ?: return null

        val incorrectBreeds = breeds.filter { it.id != correctBreed.id }.shuffled().take(3)

        val options = (listOf(correctBreed) + incorrectBreeds).shuffled().map {
            QuestionOption(
                id = it.id,
                text = it.name
            )
        }

        return Question(
            id = UUID.randomUUID().toString(),
            type = QuestionType.GUESS_THE_FACT,
            imageUrl = photo.url,
            text = "Koja je rasa mačke?",
            options = options,
            correctAnswerId = correctBreed.id
        )
    }

    private fun generateOddOneOutQuestion(
        breeds: List<Breed>,
        allBreedPhotos: List<BreedPhoto>,
        usedPhotos: MutableSet<String>
    ): Question? {
        val availablePhotos = allBreedPhotos.filter { it.photoId !in usedPhotos }
        if (availablePhotos.isEmpty()) return null

        val photo = availablePhotos.random()
        usedPhotos.add(photo.photoId)

        val correctBreed = breeds.find { it.id == photo.breedId } ?: return null

        val correctTemperaments = correctBreed.temperament.split(", ").map { it.trim() }.toSet()

        if (correctTemperaments.size < 3) return null // treba da imamo bar 3 validna temperaenta

        val incorrectTemperament = breeds
            .filter { it.id != correctBreed.id }
            .flatMap { it.temperament.split(", ").map { t -> t.trim() } }
            .filter { it !in correctTemperaments }
            .shuffled()
            .firstOrNull() ?: return null

        val options = (correctTemperaments.shuffled().take(3) + incorrectTemperament)
            .shuffled()
            .map { temperament ->
                QuestionOption(
                    id = temperament,
                    text = temperament
                )
            }

        return Question(
            id = UUID.randomUUID().toString(),
            type = QuestionType.ODD_ONE_OUT,
            imageUrl = photo.url,
            text = "Izbaci temperament koji NE pripada ovoj mački",
            options = options,
            correctAnswerId = incorrectTemperament
        )
    }
}