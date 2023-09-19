package com.croumy.hltb_wearos.presentation.ui.startapp

import android.content.Context
import androidx.lifecycle.ViewModel
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.models.Category
import com.croumy.hltb_wearos.presentation.models.Storefront
import com.croumy.hltbwearos.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class StartAppViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    val appService: AppService,
    private val hltbService: HLTBService
) : ViewModel() {
    suspend fun initWithUserData() {
        val user = hltbService.getUser()

        // INIT STOREFRONTS ORDER (USER'S FIRST)
        val userStorefront = Storefront.all.filter { (user?.user_platforms?.storefront ?: emptyList()).contains(it.value) }.sortedBy { it.value }.toSet()
        Storefront.all = userStorefront.plus(Storefront.all.toMutableList().minus(userStorefront)).toList()
        Storefront.allWithNoneOption = listOf(context.getString(R.string.none)).plus(Storefront.all.map { it.value })

        // INIT CUSTOM CATEGORIES
        Category.Custom.label = user?.set_customtab
        Category.Custom2.label = user?.set_customtab2
        Category.Custom3.label = user?.set_customtab3
    }
}