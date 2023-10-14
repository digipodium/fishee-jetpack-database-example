package com.example.fishee.ui.screens.fish

import com.example.fishee.data.local.models.Fish

enum class FishSaveStatus {
    SAVED,
    NOT_SAVED,
    EMPTY,
    INVALID
}


data class FishUiState(
    val fishList: List<Fish> = emptyList(),
    val fishName: String = "",
    val fishWeight: String = "",
    val fishHeight: String = "",
    val fishLength: String = "",
    val fishImage: String = "",
    val fishDescription: String = "",
    val fishRiver: String = "",
    val currentUserId: Int,
    val status: FishSaveStatus = FishSaveStatus.EMPTY,
    val sortType: FishSortType = FishSortType.NAME,
    val message: String = "",
)
