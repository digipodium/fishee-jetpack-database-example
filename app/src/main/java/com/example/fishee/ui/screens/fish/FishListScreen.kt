@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fishee.ui.screens.fish

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.fishee.R
import com.example.fishee.data.local.models.Fish
import com.example.fishee.ui.Appbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishListScreen(
    state: FishUiState,
    onEvent: (FishScreenEvent) -> Unit,
    onNavigateToAdd: () -> Unit,
    onNavigateToDetail: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(topBar = { Appbar() }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AddNewFishSection(state, onNavigateToAdd, onEvent)
            LazyVerticalGrid(
                columns = GridCells.Adaptive(200.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.fishList.isEmpty()) {
                    item {
                        Card(
                            onClick = onNavigateToAdd,
                        ) {
                            Text(
                                text = "No fish added yet",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                }
                items(state.fishList) { fish ->
                    FishListItem(
                        fish = fish,
                        onNavigateToDetail = onNavigateToDetail,
                        onEvent = onEvent,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FishListItem(
    fish: Fish,
    onNavigateToDetail: () -> Unit,
    onEvent: (FishScreenEvent) -> Unit,
    modifier: Modifier
) {
    Card(
        onClick = onNavigateToDetail,
        modifier = modifier
            .fillMaxSize()
    ) {
        Log.d("FishListItem", "FishListItem: ${fish.image}")
        GlideImage(
            model = if (fish.image.isNotEmpty()) Uri.parse(fish.image) else R.drawable.fish,
            contentDescription = fish.name,
            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .size(100.dp)
        )
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = if (fish.name.length > 15) "${
                        fish.name.substring(
                            0,
                            15
                        )
                    }..." else fish.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(text = fish.river, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    onEvent(FishScreenEvent.DeleteFish(fish))
                }, modifier = Modifier
                    .background(color = Color.Red, shape = RoundedCornerShape(8.dp))
                    .size(42.dp)
                    .padding(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

        }

    }
}


@Composable
private fun AddNewFishSection(
    state: FishUiState,
    onNavigateToAdd: () -> Unit,
    onEvent: (FishScreenEvent) -> Unit
) {
    var showFilterMenu by rememberSaveable { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(start = 16.dp)
    ) {
        Text(text = "Fish list", color = MaterialTheme.colorScheme.onPrimary)
        Spacer(modifier = Modifier.weight(1f))
        Box {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "sort by ${state.sortType.name.lowercase()}",
                    color = MaterialTheme.colorScheme.onPrimary
                )
                IconButton(onClick = {
                    showFilterMenu = !showFilterMenu
                }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            DropdownMenu(
                expanded = showFilterMenu,
                onDismissRequest = { showFilterMenu = false },
                offset = DpOffset((-16).dp, 0.dp)
            ) {
                DropdownMenuItem(
                    text = { Text(text = "Name") },
                    onClick = { onEvent(FishScreenEvent.SetSortBy(FishSortType.NAME)) })
                DropdownMenuItem(
                    text = { Text(text = "Date") },
                    onClick = { onEvent(FishScreenEvent.SetSortBy(FishSortType.DATE)) })
                DropdownMenuItem(
                    text = { Text(text = "Weight") },
                    onClick = { onEvent(FishScreenEvent.SetSortBy(FishSortType.WEIGHT)) })
            }
        }
        IconButton(onClick = onNavigateToAdd) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview
@Composable
fun FishListScreenPreview() {
    FishListScreen(
        state = FishUiState(
            currentUserId = 1
        ),
        onEvent = {},
        onNavigateToAdd = {},
        onNavigateToDetail = {}
    )
}