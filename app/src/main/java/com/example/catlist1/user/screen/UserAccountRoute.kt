package com.example.catlist1.user.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.catlist1.user.ProfileViewModel

@Composable
fun UserAccountRoute(//viska sada
    onAccountSaved: () -> Unit,
    viewModel: UserAccountViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val results = profileViewModel.filteredResults.collectAsState().value

    UserAccountScreen(
        state = state,
        eventPublisher = viewModel::setEvent,
        onAccountSaved = onAccountSaved,
        results = results
    )
}