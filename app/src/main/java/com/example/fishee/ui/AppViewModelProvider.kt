package com.example.fishee.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fishee.FisheeApplication
import com.example.fishee.ui.screens.fish.FishViewModel
import com.example.fishee.ui.screens.user.UserViewModel

object AppViewModelProvider {
    val factory = viewModelFactory {
        initializer {
            UserViewModel(
                context = fisheeApplication(),
                dao = fisheeApplication().db.userDao()
            )
        }
        initializer {
            FishViewModel(
                context = fisheeApplication(),
                dao = fisheeApplication().db.fishDao()
            )
        }
    }
}

fun CreationExtras.fisheeApplication(): FisheeApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FisheeApplication)