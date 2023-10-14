package com.example.fishee.ui.screens.fish

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fishee.R
import com.example.fishee.ui.Appbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishEditScreen(
    state: FishUiState,
    onEvent: (FishScreenEvent) -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = { Appbar(text = "Add Fish") },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                onEvent(FishScreenEvent.UpdateFish(state.fishId))
                when (state.status) {
                    FishSaveStatus.SAVED -> {
                        onEvent(FishScreenEvent.SetFishById(state.fishId))
                        onNavigate()
                    }
                    FishSaveStatus.NOT_SAVED -> onEvent(FishScreenEvent.SetMessage(message = "You have not saved the fish details"))
                    FishSaveStatus.EMPTY -> onEvent(FishScreenEvent.SetMessage(message = "Fields are empty"))
                    FishSaveStatus.INVALID -> onEvent(FishScreenEvent.SetMessage(message = "Fields are invalid"))
                }

            }) {
                Icon(imageVector = Icons.Default.Done, contentDescription = null)
                Text(text = "Update")
            }
        },
        modifier = modifier,
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Box(
            modifier.padding(paddingValues)
        ) {
            Column(
                Modifier
                    .padding(32.dp)
                    .verticalScroll(scrollState),
            ) {

                // show message
                Text(
                    text = state.message,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                if (state.fishImage.isNotEmpty()) {
                    ShowImage(state.fishImage, onEvent)
                } else {
                    showImagePicker(onEvent)
                }
                OutlinedTextField(
                    value = state.fishName,
                    onValueChange = { onEvent(FishScreenEvent.SetFishName(it)) },
                    label = { Text("Fish name") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.fish),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(24.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                )
                Row {
                    OutlinedTextField(
                        value = state.fishHeight,
                        onValueChange = { onEvent(FishScreenEvent.SetFishHeight(it)) },
                        label = { Text("Height") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ruler_height),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(24.dp)
                            )
                        },
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    OutlinedTextField(
                        value =   state.fishLength,
                        onValueChange = { onEvent(FishScreenEvent.SetFishLength(it)) },
                        label = { Text("Length") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ruler_length),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(24.dp)
                            )
                        },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        maxLines = 1,
                    )
                }
                OutlinedTextField(
                    value = state.fishWeight,
                    onValueChange = { onEvent(FishScreenEvent.SetFishWeight(it)) },
                    label = { Text("Weight") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.weight_hanging),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(24.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    maxLines = 1,
                )
                OutlinedTextField(
                    value = state.fishDescription,
                    onValueChange = { onEvent(FishScreenEvent.SetFishDescription(it)) },
                    label = { Text("Description") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.info_circle),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(24.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,

                    )
                OutlinedTextField(
                    value = state.fishRiver,
                    onValueChange = { onEvent(FishScreenEvent.SetFishRiver(it)) },
                    label = { Text("River name") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.water),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(24.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                )

            }
        }
    }
}

