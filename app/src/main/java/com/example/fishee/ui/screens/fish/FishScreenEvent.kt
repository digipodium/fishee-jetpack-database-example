package com.example.fishee.ui.screens.fish

import com.example.fishee.data.local.models.Fish

sealed interface FishScreenEvent {
    object NavigateBack : FishScreenEvent
    object SaveFish : FishScreenEvent
    data class DeleteFish(val fish: Fish) : FishScreenEvent
    data class SetFishName(val fishName: String) : FishScreenEvent
    data class SetFishWeight(val fishWeight: String) : FishScreenEvent
    data class SetFishHeight(val fishHeight: String) : FishScreenEvent
    data class SetFishLength(val fishLength: String) : FishScreenEvent
    data class SetFishImage(val fishImage: String) : FishScreenEvent
    data class SetFishDescription(val fishDescription: String) : FishScreenEvent
    data class SetFishRiver(val fishRiver: String) : FishScreenEvent
    data class SetMessage(val message: String) : FishScreenEvent
    data class SetSortBy(val sortBy: FishSortType) : FishScreenEvent
}
