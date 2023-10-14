package com.example.fishee.ui.screens.fish

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishee.FisheeApplication
import com.example.fishee.data.local.FishDao
import com.example.fishee.data.local.models.Fish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class FishSortType {
    NAME,
    DATE,
    WEIGHT
}

class FishViewModel(
    private val context: FisheeApplication, private val dao: FishDao
) : ViewModel() {

    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    private fun getCurrentUserId() = prefs.getInt("userId", 1)

    private val _sortType = MutableStateFlow(FishSortType.NAME)
    private val _fishes = _sortType.flatMapLatest {
        when (it) {
            FishSortType.NAME -> dao.getFishesByName(getCurrentUserId())
            FishSortType.DATE -> dao.getFishesByDate(getCurrentUserId())
            FishSortType.WEIGHT -> dao.getFishesByWeight(getCurrentUserId())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(FishUiState(currentUserId = getCurrentUserId()))
    val state = combine(_fishes, _sortType, _state) { fishes, sortType, state ->
        state.copy(fishList = fishes, sortType = sortType)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FishUiState(currentUserId = getCurrentUserId())
    )

    private fun saveFish() {
        val name = _state.value.fishName
        val weight = _state.value.fishWeight
        val height = _state.value.fishHeight
        val length = _state.value.fishLength
        val image = _state.value.fishImage
        val description = _state.value.fishDescription
        val river = _state.value.fishRiver
        val userId = _state.value.currentUserId
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
                    resetStateData()
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

    private fun resetStateData() {
        _state.update {
            it.copy(
                fishName = "",
                fishWeight = "",
                fishHeight = "",
                fishLength = "",
                fishImage = "",
                fishDescription = "",
                fishRiver = ""
            )
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
        Log.d("FishViewModel", "validateFish: $name $weight $height $length $image $description $river")
        val ans= name.isNotBlank() && weight.isNotBlank() && height.isNotBlank() && length.isNotBlank() && image.isNotEmpty() && description.isNotBlank() && river.isNotBlank()
        Log.d("FishViewModel", "validateFish: $ans")
        return ans
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
            is FishScreenEvent.SetMessage -> _state.update { it.copy(message=event.message) }
            is FishScreenEvent.SetSortBy -> _sortType.update { event.sortBy }
        }
    }
}