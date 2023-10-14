@file:OptIn(ExperimentalGlideComposeApi::class, ExperimentalGlideComposeApi::class)

package com.example.fishee.ui.screens.fish

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun FishDetailScreen(
    state: FishUiState,
    onEvent: (FishScreenEvent) -> Unit,
    onNavigateToEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        Log.d("FishDetailScreen", "FishDetailScreen: ${state.fish}")
        GlideImage(
            model = Uri.parse(state.fish?.image),
            contentDescription = state.fish?.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = state.fish?.name ?: "No name")
        Button(onClick = {
            onEvent(FishScreenEvent.SetFishFields(state.fish!!))
            onNavigateToEdit()
        }) {
            Text(text = "Edit")
        }
    }
}