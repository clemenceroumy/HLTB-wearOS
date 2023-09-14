package com.croumy.hltb_wearos.presentation.ui.home.addgame

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.ui.home.addgame.models.AddGameStep
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddGameViewModel @Inject constructor(
    private val hltbService: HLTBService,
) : ViewModel() {
    val currentStep = mutableStateOf(AddGameStep.PLATFORM)
}