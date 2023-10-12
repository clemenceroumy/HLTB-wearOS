package com.croumy.hltb_wearos.presentation.models.api

import android.content.res.Resources
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.croumy.hltb_wearos.presentation.MainActivity
import com.croumy.hltbwearos.R
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val color: String,
    val title: String,
    val category: String,
    val count: Int,
    val pageCurrent: Int,
    val pageTotal: Int,
    val pageSize: Int,
    @SerializedName("data") var games: List<GameInfo>,
    val userData: List<UserData>
) {
    constructor(original: SearchResponse) : this(
        original.color,
        original.title,
        original.category,
        original.count,
        original.pageCurrent,
        original.pageTotal,
        original.pageSize,
        original.games.toMutableList(),
        original.userData
    ) {
        assignUserGames()
    }

    private fun assignUserGames() {
        games.map { game ->
            game.isInUserList = userData.any { it.game_id == game.game_id }
            game
        }
    }
}

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
) : Parcelable {
    val picture: String get() = "https://howlongtobeat.com/games/$game_image"
    var isInUserList: Boolean = false
    private val platforms get(): List<String> = profile_platform.split(",").map { it.trim() }
    val platformsWithNoneOption get(): List<String> = listOf(MainActivity.context.getString(R.string.none)).plus(platforms)

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt()
    ) {
        isInUserList = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(count)
        parcel.writeInt(game_id)
        parcel.writeString(game_name)
        parcel.writeInt(game_name_date)
        parcel.writeString(game_alias)
        parcel.writeString(game_type)
        parcel.writeString(game_image)
        parcel.writeInt(comp_lvl_combine)
        parcel.writeInt(comp_lvl_sp)
        parcel.writeInt(comp_lvl_co)
        parcel.writeInt(comp_lvl_mp)
        parcel.writeInt(comp_lvl_spd)
        parcel.writeInt(comp_main)
        parcel.writeInt(comp_plus)
        parcel.writeInt(comp_100)
        parcel.writeInt(comp_all)
        parcel.writeInt(comp_main_count)
        parcel.writeInt(comp_plus_count)
        parcel.writeInt(comp_100_count)
        parcel.writeInt(comp_all_count)
        parcel.writeInt(invested_co)
        parcel.writeInt(invested_mp)
        parcel.writeInt(invested_co_count)
        parcel.writeInt(invested_mp_count)
        parcel.writeInt(count_comp)
        parcel.writeInt(count_speedrun)
        parcel.writeInt(count_backlog)
        parcel.writeInt(count_review)
        parcel.writeInt(review_score)
        parcel.writeInt(count_playing)
        parcel.writeInt(count_retired)
        parcel.writeString(profile_dev)
        parcel.writeInt(profile_popular)
        parcel.writeInt(profile_steam)
        parcel.writeString(profile_platform)
        parcel.writeInt(release_world)
        parcel.writeByte(if (isInUserList) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GameInfo> {
        override fun createFromParcel(parcel: Parcel): GameInfo {
            return GameInfo(parcel)
        }

        override fun newArray(size: Int): Array<GameInfo?> {
            return arrayOfNulls(size)
        }
    }
}

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