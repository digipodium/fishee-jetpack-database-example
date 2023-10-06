package com.example.fishee.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
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
    UserScreen,
    FishList,
    FishAdd,
    FishDetail
}

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
) {

    val userViewModel = AppViewModelProvider.factory.create(UserViewModel::class.java)
    val fishViewModel = AppViewModelProvider.factory.create(FishViewModel::class.java)
    val fishState = fishViewModel.state.collectAsState()
    val userState = userViewModel.state.collectAsState()
    NavHost(
        navController = rememberNavController(),
        startDestination = NavigationScreen.UserScreen.name
    ) {
        composable(NavigationScreen.UserScreen.name) {
            UserScreen(
                state = userState.value,
                onEvent = userViewModel::onEvent,
                modifier = modifier
            )
        }
        composable(NavigationScreen.FishList.name) {
            FishListScreen(
                state = fishState.value,
                onEvent = fishViewModel::onEvent,
                modifier = modifier
            )
        }
        composable(NavigationScreen.FishAdd.name) {
            FishAddScreen(
                state = fishState.value,
                onEvent = fishViewModel::onEvent,
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

