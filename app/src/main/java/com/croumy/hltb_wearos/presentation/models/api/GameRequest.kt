package com.croumy.hltb_wearos.presentation.models.api

import com.croumy.hltb_wearos.presentation.models.Constants.Companion.GET_GAMES_LIMIT
import com.croumy.hltb_wearos.presentation.models.categories


data class GameRequest(
    val lists: List<String> = categories.map { it.value },
    val set_playstyle: String = "comp_all",
    var limit: String = "$GET_GAMES_LIMIT",
    val name: String = ""
)