package com.example.fishee.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fishee.ui.screens.fish.FishAddScreen
import com.example.fishee.ui.screens.fish.FishDetailScreen
import com.example.fishee.ui.screens.fish.FishListScreen
import com.example.fishee.ui.screens.fish.FishViewModel
import com.example.fishee.ui.screens.user.UserScreen
import com.example.fishee.ui.screens.user.UserViewModel


enum class NavigationScreen {
    UserScreen, FishList, FishAdd, FishDetail
}

@Composable
fun Navigation(

    modifier: Modifier = Modifier,
) {
    val userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.factory)
    val fishViewModel: FishViewModel = viewModel(factory = AppViewModelProvider.factory)
    val fishState = fishViewModel.state.collectAsState()
    val userState = userViewModel.state.collectAsState()
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = NavigationScreen.FishList.name
    ) {
        composable(NavigationScreen.FishList.name) {
            UserScreen(
                state = userState.value, onEvent = userViewModel::onEvent,
                onNavigate = { navController.navigate(NavigationScreen.FishList.name) },
                modifier = modifier
            )
        }
        composable(NavigationScreen.FishList.name) {
            FishListScreen(
                state = fishState.value,
                onEvent = fishViewModel::onEvent,
                onNavigateToAdd = {
                    navController.navigate(NavigationScreen.FishAdd.name)
                },
                onNavigateToDetail = {
                    navController.navigate(NavigationScreen.FishDetail.name)
                },
                modifier = modifier
            )
        }
        composable(NavigationScreen.FishAdd.name) {
            FishAddScreen(
                state = fishState.value,
                onEvent = fishViewModel::onEvent,
                onNavigate = {
                    navController.navigate(NavigationScreen.FishList.name)
                },
                modifier = modifier
            )
        }
        composable(NavigationScreen.FishDetail.name) {
            FishDetailScreen(
                state = fishState.value,
                onEvent = fishViewModel::onEvent,
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Appbar(
    modifier: Modifier = Modifier,
    text: String = "Fishee",
    onNavigateBack: () -> Unit = {},
    isBackEnabled: Boolean = false
) {
    TopAppBar(
        title = {
            Text(text = text)
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            if (isBackEnabled) {
                IconButton(onClick = {
                    onNavigateBack
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Back"
                    )
                }
            }
        },
        modifier = modifier
    )
}

