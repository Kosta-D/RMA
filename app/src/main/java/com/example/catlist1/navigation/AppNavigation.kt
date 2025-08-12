package com.example.catlist1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.catlist1.breeds.details.BreedDetailsScreen
import com.example.catlist1.breeds.details.BreedDetailsViewModel
import com.example.catlist1.breeds.gallery.BreedGalleryScreen
import com.example.catlist1.breeds.gallery.BreedGalleryViewModel
import com.example.catlist1.breeds.gallery.BreedPhotoViewerScreen
import com.example.catlist1.breeds.list.BreedListScreen
import com.example.catlist1.breeds.list.BreedListViewModel
import com.example.catlist1.home.HomeScreen
import com.example.catlist1.leaderboard.LeaderboardScreen
import com.example.catlist1.leaderboard.storage.QuizResultViewModel
import com.example.catlist1.quiz.QuizResultScreen
import com.example.catlist1.quiz.QuizScreen
import com.example.catlist1.user.ProfileViewModel
import com.example.catlist1.user.account.UserAccountStore
import com.example.catlist1.user.screen.UserAccountScreen
import com.example.catlist1.user.screen.UserAccountViewModel


const val BREED_ID_ARG = "breedId"
const val SCORE_ARG = "score"
const val PHOTO_ID_ARG = "photoId"
const val TIMESTAMP_ARG = "timestamp"



@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        homeScreen(
            route = "home",
            navController = navController
        )

        userAccountScreen(
            route = "account",
            navController = navController
        )

        breedListScreen(
            route = "breeds",
            navController = navController
        )

        breedDetailsScreen(
            route = "breeds/{$BREED_ID_ARG}",
            arguments = listOf(
                navArgument(BREED_ID_ARG) {
                    type = NavType.StringType
                    nullable = false
                }
            ),
            navController = navController
        )

        galleryScreen(
            route = "breeds/{$BREED_ID_ARG}/gallery",
            arguments = listOf(
                navArgument(BREED_ID_ARG) {
                    type = NavType.StringType
                    nullable = false
                }
            ),
            navController = navController
        )

        quizScreen(
            route = "quiz",
            navController = navController
        )

        quizResultScreen(
            route = "quiz/result?score={$SCORE_ARG}&timestamp={$TIMESTAMP_ARG}",
            arguments = listOf(
                navArgument(SCORE_ARG) {
                    type = NavType.FloatType
                    nullable = false
                },
                navArgument(TIMESTAMP_ARG) {
                    type = NavType.LongType
                    nullable = false
                }
            ),
            navController = navController
        )

        leaderboardScreen(
            route = "leaderboard",
            navController = navController
        )

        profileScreen(
            route = "profile",
            navController = navController
        )

        photoViewerScreen(
            route = "breeds/{$BREED_ID_ARG}/gallery/photoViewer/{$PHOTO_ID_ARG}",
            arguments = listOf(
                navArgument(BREED_ID_ARG) {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument(PHOTO_ID_ARG) {
                    type = NavType.StringType
                    nullable = false
                }
            ),
            navController = navController
        )

    }

    /*stari

    NavHost(
        navController = navController,
        startDestination = "breeds"
    ) {
        breedListScreen(
            route = "breeds",
            navController = navController
        )

        breedDetailsScreen(
            route = "breeds/{$BREED_ID_ARG}",
            arguments = listOf(
                navArgument(BREED_ID_ARG) {
                    type = NavType.StringType
                    nullable = false
                }
            ),
            navController = navController
        )
    }

     */
}

private fun NavController.navigateToBreedDetails(breedId: String) {
    this.navigate(route = "breeds/$breedId")
}
private fun NavController.navigateToBreedList() {
    this.navigate(route = "breeds")
}

private fun NavController.navigateToQuiz() {
    this.navigate("quiz") // možeš staviti prazno screen za sad
}

private fun NavController.navigateToLeaderboard() {
    this.navigate("leaderboard")
}

private fun NavController.navigateToProfile() {
    this.navigate("profile")
}
private fun NavController.navigateToUserAccount() {
    this.navigate("account")
}

private fun NavController.navigateToPhotoViewer(breedId: String, photoId: String) {
    println("Navigating to viewer with photoId=$photoId")
    this.navigate("breeds/$breedId/gallery/photoViewer/$photoId")
}

private fun NavGraphBuilder.photoViewerScreen(
    route: String,
    arguments: List<NamedNavArgument>,
    navController: NavController
) = composable(route = route, arguments = arguments) { backStackEntry ->
    val breedId = requireNotNull(backStackEntry.arguments?.getString(BREED_ID_ARG))
    val photoId = requireNotNull(backStackEntry.arguments?.getString(PHOTO_ID_ARG))

    val viewModel = hiltViewModel<BreedGalleryViewModel>()

    BreedPhotoViewerScreen(
        viewModel = viewModel,
        initialPhotoId = photoId,
        onClose = { navController.navigateUp() }
    )
}



private fun NavGraphBuilder.homeScreen(
    route: String,
    navController: NavController
) = composable(route = route) {
    val userAccountStore = hiltViewModel<UserAccountViewModel>()
    val userAccount = userAccountStore.userAccount.collectAsState()

    LaunchedEffect(userAccount.value) {
        if (userAccount.value == null) {
            navController.navigate("account") {
                popUpTo("home") { inclusive = true } // može i bez ovoga
            }
        }
    }

    if (userAccount.value != null) {
        HomeScreen(
            isAccountMissing = false,
            onNavigateToAccount = { navController.navigateToUserAccount() },
            onBreedsClick = { navController.navigateToBreedList() },
            onQuizClick = { navController.navigateToQuiz() },
            onLeaderboardClick = { navController.navigateToLeaderboard() },
            onProfileClick = { navController.navigateToProfile() },
            onResetProfile = { userAccountStore.clearUserAccount()}
        )
    }
}





private fun NavGraphBuilder.userAccountScreen(
    route: String,
    navController: NavController
) = composable(route = route) {
    val viewModel = hiltViewModel<UserAccountViewModel>()
    val state = viewModel.state.collectAsState()


    val quizResultStore = hiltViewModel<QuizResultViewModel>()
    val results = quizResultStore.results.collectAsState()

    UserAccountScreen(
        state = state.value,
        eventPublisher = { viewModel.setEvent(it) },
        onAccountSaved = {
            navController.navigate("home") {
                popUpTo("account") { inclusive = true } // da se ne vraća nazad na formu
            }
        },
        results = results.value


    )


}



private fun NavGraphBuilder.galleryScreen(
    route: String,
    arguments: List<NamedNavArgument>,
    navController: NavController
) = composable(route = route, arguments = arguments) { backStackEntry ->
    val breedId = requireNotNull(backStackEntry.arguments?.getString(BREED_ID_ARG))
    val viewModel = hiltViewModel<BreedGalleryViewModel>()

    BreedGalleryScreen(
        viewModel = viewModel,
        breedId = breedId,
        onBack = { navController.navigateUp() },
        onPhotoClick = { photoId ->
            navController.navigateToPhotoViewer(breedId, photoId)
        }
    )
}

private fun NavGraphBuilder.quizScreen(
    route: String,
    navController: NavController
) = composable(route = route) {
    QuizScreen(
        onNavigateToResult = { score,timestamp ->
            navController.navigate("quiz/result?score=$score&timestamp=$timestamp")
        },
        onBackToHome = {
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
        }
    )
}




private fun NavGraphBuilder.quizResultScreen(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    navController: NavController
) = composable(route = route, arguments = arguments) { backStackEntry ->
    val score = requireNotNull(backStackEntry.arguments?.getFloat(SCORE_ARG))
    val timestamp = requireNotNull(backStackEntry.arguments?.getLong(TIMESTAMP_ARG))

    val quizResultViewModel = hiltViewModel<QuizResultViewModel>()
    val allResults = quizResultViewModel.results.collectAsState()

    val result = allResults.value.find { it.timestamp == timestamp }

    QuizResultScreen(
        score = score,
        timestamp = timestamp,
        isPublished = result?.published ?: false,
        onBackToHome = {
            navController.navigate("home") {
                popUpTo("quiz") { inclusive = true }
            }
        },
        onViewLeaderboard = {
            navController.navigate("leaderboard")
        },
        onPublish = {
            quizResultViewModel.publishResult(result)
        }
    )
}


private fun NavGraphBuilder.leaderboardScreen(
    route: String,
    navController: NavController
) = composable(route = route) {
    LeaderboardScreen(
        onBack = {navController.navigateUp()}
    )

}

private fun NavGraphBuilder.profileScreen(
    route: String,
    navController: NavController
) = composable(route = route) {
    val viewModel = hiltViewModel<UserAccountViewModel>()
    val profileViewModel = hiltViewModel<ProfileViewModel>()

    val state = viewModel.state.collectAsState()
    val filteredResults = profileViewModel.filteredResults.collectAsState()

    UserAccountScreen(
        state = state.value,
        eventPublisher = { viewModel.setEvent(it) },
        onAccountSaved = {
            navController.navigate("home") {
                popUpTo("profile") { inclusive = true }
            }
        },
        results = filteredResults.value // ✅ koristi samo korisnikove rezultate
    )
}


//staro

private fun NavGraphBuilder.breedListScreen(
    route: String,
    navController: NavController
) = composable(route = route) {
    val viewModel = hiltViewModel<BreedListViewModel>()
    BreedListScreen(
        viewModel = viewModel,
        onBreedClick = { breedId ->
            navController.navigateToBreedDetails(breedId)
        }
    )
}


private fun NavGraphBuilder.breedDetailsScreen(
    route: String,
    arguments: List<NamedNavArgument>,
    navController: NavController
) = composable(route = route, arguments = arguments) { backStackEntry ->
    val breedId = requireNotNull(backStackEntry.arguments?.getString(BREED_ID_ARG))
    val viewModel = hiltViewModel<BreedDetailsViewModel>()

    BreedDetailsScreen(
        viewModel = viewModel,
        breedId = breedId,
        onBack = { navController.navigateUp() },
        onOpenGallery = {breedId ->
            navController.navigate("breeds/$breedId/gallery")
        }
    )










    /*
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "breeds"
    ) {
        composable("breeds") {
            val viewModel = hiltViewModel<BreedListViewModel>()
            BreedListScreen(
                viewModel = viewModel,
                onBreedClick = { breedId ->
                    navController.navigate("breeds/$breedId")
                }
            )
        }

        composable(
            route = "breeds/{$BREED_ID_ARG}",
            arguments = listOf(
                navArgument(BREED_ID_ARG) {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val breedId = backStackEntry.arguments?.getString(BREED_ID_ARG)!!
            BreedDetailsScreen(
                breedId = breedId,
                onBack = { navController.navigateUp() }
            )
        }
    }
     */

}