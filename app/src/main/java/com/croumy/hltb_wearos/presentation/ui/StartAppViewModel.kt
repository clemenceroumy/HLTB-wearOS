package com.croumy.hltb_wearos.presentation.ui

import androidx.lifecycle.ViewModel
import com.croumy.hltb_wearos.presentation.data.PreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartAppViewModel @Inject constructor(val preferencesService: PreferencesService): ViewModel()