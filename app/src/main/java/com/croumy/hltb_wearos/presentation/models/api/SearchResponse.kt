package com.croumy.hltb_wearos.presentation.models.api

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val color: String,
    val title: String,
    val category: String,
    val count: Int,
    val pageCurrent: Int,
    val pageTotal: Int,
    val pageSize: Int,
    @SerializedName("data") val games: List<GameInfo>,
    val userData: List<UserData>
)

data class GameInfo(
    val count: Int,
    val game_id: Int,
    val game_name: String,
    val game_name_date: Int,
    val game_alias: String,
    val game_type: String,
    val game_image: String,
    val comp_lvl_combine: Int,
    val comp_lvl_sp: Int,
    val comp_lvl_co: Int,
    val comp_lvl_mp: Int,
    val comp_lvl_spd: Int,
    val comp_main: Int,
    val comp_plus: Int,
    val comp_100: Int,
    val comp_all: Int,
    val comp_main_count: Int,
    val comp_plus_count: Int,
    val comp_100_count: Int,
    val comp_all_count: Int,
    val invested_co: Int,
    val invested_mp: Int,
    val invested_co_count: Int,
    val invested_mp_count: Int,
    val count_comp: Int,
    val count_speedrun: Int,
    val count_backlog: Int,
    val count_review: Int,
    val review_score: Int,
    val count_playing: Int,
    val count_retired: Int,
    val profile_dev: String,
    val profile_popular: Int,
    val profile_steam: Int,
    val profile_platform: String,
    val release_world: Int
)

data class UserData(
    val game_id: Int,
    val user_playing: Int,
    val user_backlog: Int,
    val user_replays: Int,
    val user_custom: Int,
    val user_custom2: Int,
    val user_custom3: Int,
    val user_comp: Int,
    val user_retired: Int
)