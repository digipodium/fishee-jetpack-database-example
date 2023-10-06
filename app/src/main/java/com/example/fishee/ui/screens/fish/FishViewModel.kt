package com.example.fishee.ui.screens.fish

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishee.FisheeApplication
import com.example.fishee.data.local.FishDao
import com.example.fishee.data.local.models.Fish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FishViewModel(
    private val context: FisheeApplication, private val dao: FishDao
) : ViewModel() {

    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    private fun getCurrentUserId() = prefs.getInt("userId", 1)

    private val _state = MutableStateFlow(FishUiState(currentUserId = getCurrentUserId()))
    val state: StateFlow<FishUiState> = _state

    private fun saveFish() {
        val name = state.value.fishName
        val weight = state.value.fishWeight
        val height = state.value.fishHeight
        val length = state.value.fishLength
        val image = state.value.fishImage
        val description = state.value.fishDescription
        val river = state.value.fishRiver
        val userId = state.value.currentUserId
        if (validateFish(name, weight, height, length, image, description, river)) {
            viewModelScope.launch {
                try {
                    dao.upsertFish(
                        Fish(
                            name = name,
                            weight = weight.toFloat(),
                            height = height.toFloat(),
                            length = length.toFloat(),
                            image = image,
                            description = description,
                            river = river,
                            userId = userId
                        )
                    )
                    _state.update {
                        it.copy(status = FishSaveStatus.SAVED)
                    }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(status = FishSaveStatus.NOT_SAVED)
                    }
                }
            }
        } else {
            _state.update {
                it.copy(status = FishSaveStatus.INVALID)
            }
        }
    }

    private fun validateFish(
        name: String,
        weight: String,
        height: String,
        length: String,
        image: String,
        description: String,
        river: String
    ): Boolean {
        return name.isNotBlank() && weight.isNotBlank() && height.isNotBlank() && length.isNotBlank() && image.isNotBlank() && description.isNotBlank() && river.isNotBlank()
    }

    private fun deleteFish(fish: Fish) {
        viewModelScope.launch {
            dao.deleteFish(fish)
        }
    }

    init {
        viewModelScope.launch {
            dao.getFishesByName(getCurrentUserId()).collect { fishes ->
                _state.update {
                    it.copy(
                        fishList = fishes
                    )
                }
            }
        }
    }

    fun onEvent(event: FishScreenEvent) {
        when (event) {
            FishScreenEvent.SaveFish -> saveFish()
            FishScreenEvent.NavigateBack -> _state.update { it.copy(status = FishSaveStatus.EMPTY) }
            is FishScreenEvent.DeleteFish -> deleteFish(event.fish)
            is FishScreenEvent.SetFishDescription -> _state.update { it.copy(fishDescription = event.fishDescription) }
            is FishScreenEvent.SetFishHeight -> _state.update { it.copy(fishHeight = event.fishHeight) }
            is FishScreenEvent.SetFishImage -> _state.update { it.copy(fishImage = event.fishImage) }
            is FishScreenEvent.SetFishLength -> _state.update { it.copy(fishLength = event.fishLength) }
            is FishScreenEvent.SetFishName -> _state.update { it.copy(fishName = event.fishName) }
            is FishScreenEvent.SetFishRiver -> _state.update { it.copy(fishRiver = event.fishRiver) }
            is FishScreenEvent.SetFishWeight -> _state.update { it.copy(fishWeight = event.fishWeight) }
        }
    }
}