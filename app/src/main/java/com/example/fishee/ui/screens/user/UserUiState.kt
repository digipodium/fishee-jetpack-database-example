package com.example.fishee.ui.screens.user

import com.example.fishee.data.local.models.User
enum class UserSaveStatus {
    SAVED,
    NOT_SAVED,
    EMPTY,
    INVALID
}

data class UserUiState(
    val userList: List<User> = emptyList(),
    val userName: String = "",
    val userPassword: String = "",
    val status: UserSaveStatus = UserSaveStatus.EMPTY,
    val isDialogVisible: Boolean = false,
)
