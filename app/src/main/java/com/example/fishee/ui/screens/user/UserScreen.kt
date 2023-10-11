@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fishee.ui.screens.user

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.fishee.data.local.models.User
import com.example.fishee.ui.Appbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    state: UserUiState,
    onEvent: (UserScreenEvent) -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { Appbar() },
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.isDialogVisible) {
                    AddUserDialog(state = state, onEvent = onEvent)
                }
                var isVisible by rememberSaveable { mutableStateOf(false) }
                Button(
                    onClick = {
                        isVisible = !isVisible
                        onEvent(UserScreenEvent.ShowUsers)
                    }) {
                    Text(text = if (isVisible) "Select a user" else "Login")
                }

                AnimatedVisibility(visible = isVisible) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LazyRow(
                            userScrollEnabled = true,
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            state = rememberLazyListState()
                        ) {
                            items(state.userList) {
                                UserCard(user = it, onEvent = onEvent, onNavigate = onNavigate)
                            }
                            item {
                                AddUserCard(onEvent = onEvent)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddUserCard(onEvent: (UserScreenEvent) -> Unit) {
    Card(
        onClick = {
            onEvent(UserScreenEvent.ShowAddDialog)
        },
        shape = CircleShape,
        modifier = Modifier
            .padding(8.dp)
            .size(64.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add user"
            )
        }
    }
}

@Composable
fun UserCard(user: User, onEvent: (UserScreenEvent) -> Unit, onNavigate: () -> Unit) {
    Box {
        Card(
            onClick = {
                onEvent(UserScreenEvent.SetCurrentUser(user.id))
                onNavigate()
            },
            shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(70.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = user.name.take(4))
            }
        }
        IconButton(
            onClick = {
                onEvent(UserScreenEvent.DeleteUser(user))
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(
                    MaterialTheme.colorScheme.error,
                    shape = CircleShape
                )
                .height(24.dp)
                .width(24.dp),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Icon(imageVector = Icons.Default.Clear, contentDescription = "Delete user")
        }
    }
}

@Composable
fun AddUserDialog(
    state: UserUiState, onEvent: (UserScreenEvent) -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = {
            onEvent(UserScreenEvent.DismissDialog)
        },
        confirmButton = {
            IconButton(onClick = {
                onEvent(UserScreenEvent.SaveUser)
            }) {
                Icon(
                    imageVector = Icons.Default.Done, contentDescription = "Save user"
                )
            }
        },
        title = {
            Text(text = "Add new user")
        },
        text = {
            Column {
                OutlinedTextField(value = state.userName,
                    onValueChange = { onEvent(UserScreenEvent.SetUserName(it)) },
                    placeholder = { Text(text = "Username") })
                OutlinedTextField(
                    value = state.userPassword,
                    onValueChange = { onEvent(UserScreenEvent.SetUserPassword(it)) },
                    placeholder = { Text(text = "Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        },
    )
}
