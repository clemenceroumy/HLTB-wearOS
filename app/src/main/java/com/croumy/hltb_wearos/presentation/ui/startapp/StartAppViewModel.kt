package com.croumy.hltb_wearos.presentation.ui.startapp

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.data.PreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartAppViewModel @Inject constructor(
    val appService: AppService
): ViewModel()