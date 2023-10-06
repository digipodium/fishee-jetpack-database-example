package com.example.fishee.ui.screens.user

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishee.FisheeApplication
import com.example.fishee.data.local.UserDao
import com.example.fishee.data.local.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    private val context: FisheeApplication,
    private val dao: UserDao
) : ViewModel() {

    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)


    private val _state = MutableStateFlow(UserUiState())
    val state: StateFlow<UserUiState> = _state

    private fun saveUser() {
        val name = state.value.userName
        val password = state.value.userPassword
        viewModelScope.launch {
            try {
                dao.upsertUser(User(name = name, password = password))
                _state.update { it.copy(status = UserSaveStatus.SAVED, isDialogVisible = false) }
            } catch (e: Exception) {
                _state.update { it.copy(status = UserSaveStatus.NOT_SAVED) }
            }
        }
    }

    private fun validateUser(user: User): Boolean {
        if (user.name.isEmpty() || user.password.isEmpty()) {
            _state.update { it.copy(status = UserSaveStatus.INVALID) }
            return false
        }
        return true
    }

    private fun loadUsers() {
        viewModelScope.launch {
            dao.getUsers().collect { users ->
                _state.update { it.copy(userList = users) }
            }
        }
    }

    fun onEvent(event: UserScreenEvent) {
        when (event) {
            UserScreenEvent.NavigateToFishList -> _state.update { it.copy(status = UserSaveStatus.EMPTY) }
            UserScreenEvent.SaveUser -> saveUser()
            is UserScreenEvent.SetCurrentUser -> prefs.edit().putInt("userId", event.userId).apply()
            is UserScreenEvent.SetUserName -> _state.update { it.copy(userName = event.userName) }
            is UserScreenEvent.SetUserPassword -> _state.update { it.copy(userPassword = event.userPassword) }
            UserScreenEvent.ShowUsers -> loadUsers()
            UserScreenEvent.DismissDialog -> _state.update { it.copy(isDialogVisible = false) }
            UserScreenEvent.ShowAddDialog -> _state.update { it.copy(isDialogVisible = true) }
            is UserScreenEvent.DeleteUser -> deleteUser(event.user)
        }
    }

    private fun deleteUser(user: User) {
        viewModelScope.launch {
            dao.deleteUser(user)
        }
    }


}