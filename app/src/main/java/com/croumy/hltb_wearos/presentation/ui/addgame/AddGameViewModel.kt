package com.croumy.hltb_wearos.presentation.ui.addgame

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.models.Storefront
import com.croumy.hltb_wearos.presentation.models.api.AddGameRequest
import com.croumy.hltb_wearos.presentation.models.api.Category
import com.croumy.hltb_wearos.presentation.models.api.GameInfo
import com.croumy.hltb_wearos.presentation.models.api.QuickAdd
import com.croumy.hltb_wearos.presentation.ui.addgame.models.AddGameStep
import com.croumy.hltbwearos.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGameViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val hltbService: HLTBService,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    lateinit var game: MutableStateFlow<GameInfo?>
    private val addGameRequest: MutableState<AddGameRequest?> = mutableStateOf(null)

    val currentStep = mutableStateOf(AddGameStep.PLATFORM)
    val selectedPlatform = mutableStateOf("")
    val selectedStorefront = mutableStateOf("")
    val selectedCategory = mutableStateOf("")

    val isAdding: MutableState<Boolean?> = mutableStateOf(null)

    fun init(game: GameInfo) {
        this.game = MutableStateFlow(game)
        selectedPlatform.value = game.platformsWithNoneOption[0]
        selectedStorefront.value = Storefront.allWithNoneOption[0]
        selectedCategory.value = Category.Backlog.label
        addGameRequest.value = AddGameRequest(
            game = game,
            quickAdd = QuickAdd()
        )
    }

    fun addGame() {
        viewModelScope.launch {
            isAdding.value = true
            addGameRequest.value = addGameRequest.value?.copy(
                quickAdd = addGameRequest.value!!.quickAdd.copy(
                    platform = selectedPlatform.value,
                    storefront = selectedStorefront.value,
                    type = selectedCategory.value
                )
            )
            hltbService.addGame(addGameRequest.value!!)
            isAdding.value = false
        }
    }
}