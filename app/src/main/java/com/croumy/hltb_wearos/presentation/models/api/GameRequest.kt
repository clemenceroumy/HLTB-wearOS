package com.croumy.hltb_wearos.presentation.models.api


data class GameRequest(
    val lists: List<String> = Category.all.map { it.value },
    val set_playstyle: String = "comp_all",
    val limit: Int = 1000,
    val name: String = ""
)