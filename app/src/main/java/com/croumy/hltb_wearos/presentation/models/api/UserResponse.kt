package com.croumy.hltb_wearos.presentation.models.api

import com.croumy.hltb_wearos.presentation.models.Storefront

data class UserResponseData(
    val data : List<UserResponse>
)

data class UserResponse(
    val user_id: String,
    val user_name: String,
    val set_customtab: String,
    val set_customtab2: String,
    val set_customtab3: String,
    val user_platforms: UserPlatforms
)

data class UserPlatforms(
    val storefront: List<String>
) {
    val storefronts: List<Storefront> get() = storefront.mapNotNull { Storefront.fromValue(it) }
}