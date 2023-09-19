package com.croumy.hltb_wearos.presentation.models.api

import com.croumy.hltb_wearos.presentation.models.categories


data class GameRequest(
    val lists: List<String> = categories.map { it.value },
    val set_playstyle: String = "comp_all",
    val limit: Int = 1000,
    val name: String = ""
)