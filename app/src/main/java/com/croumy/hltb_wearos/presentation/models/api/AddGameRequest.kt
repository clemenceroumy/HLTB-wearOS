package com.croumy.hltb_wearos.presentation.models.api

import com.croumy.hltb_wearos.presentation.MainActivity
import com.croumy.hltbwearos.R

data class AddGameRequest(
    val game: GameInfo, // SAME OBJECT RETURNED BY SEARCH
    val quickAdd: QuickAdd,
)

data class QuickAdd(
    val userId: Int = 0,
    val custom: String = Category.Custom.label ?: "",
    val custom2: String = Category.Custom2.label ?: "",
    val custom3: String = Category.Custom3.label ?: "",
    val platform: String = "",
    val storefront: String = "",
    val type: String = Category.Backlog.label // == Category.label
) {
    val platformWithoutNoneOption get() = if(this.platform == MainActivity.context.getString(R.string.none)) "" else this.platform
    val storefrontWithoutNoneOption get() = if(this.storefront == MainActivity.context.getString(R.string.none)) "" else this.storefront
}