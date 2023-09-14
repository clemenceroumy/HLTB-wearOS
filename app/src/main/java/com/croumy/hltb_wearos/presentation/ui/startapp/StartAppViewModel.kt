package com.croumy.hltb_wearos.presentation.ui.startapp

import androidx.lifecycle.ViewModel
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.models.api.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartAppViewModel @Inject constructor(
    val appService: AppService,
    private val hltbService: HLTBService
) : ViewModel() {
    suspend fun initCategories() {
        val user = hltbService.getUser()
        Category.Custom.label = user?.set_customtab
        Category.Custom2.label = user?.set_customtab2
        Category.Custom3.label = user?.set_customtab3
    }
}