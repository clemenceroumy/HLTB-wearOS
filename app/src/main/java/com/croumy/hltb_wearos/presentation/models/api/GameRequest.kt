package com.croumy.hltb_wearos.presentation.models.api

import com.croumy.hltbwearos.BuildConfig


data class GameRequest(
    val lists: List<String> = Categories.values().map { it.value },
    val set_playstyle: String = "comp_all",
    val limit: Int = 1000,
    val name: String = ""
)