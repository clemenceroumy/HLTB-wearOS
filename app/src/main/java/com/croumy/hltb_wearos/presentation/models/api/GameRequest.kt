package com.croumy.hltb_wearos.presentation.models.api

data class GameRequest(
    val user_id: Int = 304670,
    val lists: List<String> = listOf("playing"),
    val set_playstyle: String = "comp_all",
    val name: String = ""
)