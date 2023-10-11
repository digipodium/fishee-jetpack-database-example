@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fishee.ui.screens.fish

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            AddNewFishSection(onNavigateToAdd)
            FilterSelectSection(onEvent = onEvent)
            LazyHorizontalGrid(
                rows = GridCells.Adaptive(200.dp), modifier = Modifier.padding(top = 16.dp)
            ) {
                item {

                }
            }
        }
    }
}

@Composable
fun FilterSelectSection(onEvent: (FishScreenEvent) -> Unit) {
    var showFilterMenu by rememberSaveable { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            showFilterMenu = !showFilterMenu
        }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
            )
        }
        DropdownMenu(
            expanded = showFilterMenu,
            onDismissRequest = { showFilterMenu = false }
        ) {
            DropdownMenuItem(text = { Text(text = "Name") }, onClick = { /*TODO*/ })
            DropdownMenuItem(text = { Text(text = "Date") }, onClick = { /*TODO*/ })
            DropdownMenuItem(text = { Text(text = "Weight") }, onClick = { /*TODO*/ })
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "Filter by: ")
    }
}


@Composable
private fun AddNewFishSection(onNavigateToAdd: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(start = 16.dp)
    ) {
        Text(text = "Add Fish 🐟🐠🐡🦈", color = MaterialTheme.colorScheme.onPrimary)
        Spacer(modifier = Modifier.weight(1f))
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