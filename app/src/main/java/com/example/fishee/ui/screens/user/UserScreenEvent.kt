package com.example.fishee.ui.screens.user

import com.example.fishee.data.local.models.User

sealed interface UserScreenEvent {
    object NavigateToFishList : UserScreenEvent
    object SaveUser : UserScreenEvent
    data class SetUserName(val userName: String) : UserScreenEvent
    data class SetUserPassword(val userPassword: String) : UserScreenEvent
    object ShowUsers : UserScreenEvent
    data class SetCurrentUser(val userId: Int) : UserScreenEvent
    object ShowAddDialog : UserScreenEvent
    object DismissDialog : UserScreenEvent
    data class DeleteUser(val user: User) : UserScreenEvent
}