package com.example.fishee.ui.screens.fish


import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fishee.R
import com.example.fishee.ui.Appbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishAddScreen(
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
                onEvent(FishScreenEvent.SaveFish)
                when (state.status) {
                    FishSaveStatus.SAVED -> onNavigate()
                    FishSaveStatus.NOT_SAVED -> onEvent(FishScreenEvent.SetMessage(message = "You have not saved the fish details"))
                    FishSaveStatus.EMPTY -> onEvent(FishScreenEvent.SetMessage(message = "Fields are empty"))
                    FishSaveStatus.INVALID -> onEvent(FishScreenEvent.SetMessage(message = "Fields are invalid"))
                }

            }) {
                Icon(imageVector = Icons.Default.Done, contentDescription = null)
                Text(text = "Save")
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
                        value = state.fishLength,
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

@Composable
private fun showImagePicker(onEvent: (FishScreenEvent) -> Unit) {
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            Log.d("Tag", "0: $uri")
            val uriString = uri.toString()
            Log.d("Tag", "1: $uriString")
            Log.d("Tag", "2: ${Uri.parse(uriString)}")
            onEvent(FishScreenEvent.SetFishImage(uri.toString()))
        })
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(128.dp)
                .padding(8.dp)
                .clip(CircleShape)
                .border(
                    2.dp,
                    color = androidx.compose.ui.graphics.Color.Gray,
                    shape = CircleShape
                )
                .clickable {
                    pickMedia.launch(
                        PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.image),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(50.dp)
            )
        }
        Text(
            text = "Add image",
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun ShowImage(image: String, onEvent: (FishScreenEvent) -> Unit) {
    Log.d("Tag", "2: ${Uri.parse(image)}")
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = Uri.parse(image),
            contentDescription = null,
            modifier = Modifier
                .size(128.dp)
                .clip(
                    CircleShape
                ),
            contentScale = ContentScale.Crop,
            onError = {
                onEvent(FishScreenEvent.SetFishImage(""))
                Log.d("Tag", "Error: $it")
            },
            onLoading = {
                Log.d("Tag", "Loading: $it")
            },
        )
        IconButton(
            onClick = { onEvent(FishScreenEvent.SetFishImage("")) },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center)
                .offset(x = 50.dp, y = 32.dp)
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.errorContainer, CircleShape)
        ) {
            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
        }
    }

}
