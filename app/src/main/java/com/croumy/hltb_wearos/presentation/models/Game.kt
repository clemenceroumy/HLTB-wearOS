package com.croumy.hltb_wearos.presentation.models

data class GameListResponse(
    val data: GameList,
)

data class GameList(
    val count: Int,
    val gamesList: List<Game>
)

data class Game(
    val id: Int,
    val custom_title: String,
    val platform: String,
    val play_storefront: String,
    val list_playing: Int,
    val list_backlog: Int,
    val list_replay: Int,
    val list_custom: Int,
    val list_custom2: Int,
    val list_custom3: Int,
    val list_comp: Int,
    val list_retired: Int,
    val comp_main: Int,
    val comp_plus: Int,
    val comp_100: Int,
    val comp_speed: Int,
    val comp_speed100: Int,
    val comp_main_notes: String,
    val comp_plus_notes: String,
    val comp_100_notes: String,
    val comp_speed_notes: String,
    val comp_speed100_notes: String,
    val invested_pro: Int,
    val invested_sp: Int,
    val invested_spd: Int,
    val invested_co: Int,
    val invested_mp: Int,
    val play_count: Int,
    val play_dlc: Int,
    val review_score: Int,
    val review_notes: String,
    val retired_notes: String,
    val date_complete: String,
    val date_updated: String,
    val play_video: String,
    val play_notes: String,
    val game_id: Int,
    val game_image: String,
    val game_type: String,
    val release_world: String,
    val comp_all: Int,
    val comp_all_g: Int,
    val review_score_g: Int
)