package com.croumy.hltb_wearos.presentation.models

import com.croumy.hltb_wearos.presentation.MainActivity
import com.croumy.hltbwearos.R

// NOT FOUND API CALL FROM HLTB WEBSITE SO I CREATED AN HARDCODED ENUM CLASS
enum class Storefront(val value: String) {
    // My Storefronts
    BORROWED("Borrowed"),
    EPIC_GAMES("Epic Games"),
    GOG("GOG"),
    NINTENDO_ESHOP("Nintendo eShop"),
    NINTENDO_ONLINE("Nintendo Online"),
    PHYSICAL("Physical"),
    STEAM("Steam"),
    XBOX_GAME_PASS("Xbox Game Pass"),

    // Digital
    AMAZON_GAME_APP("Amazon Game App"),
    AMAZON_LUNA("Amazon Luna"),
    APPLE_APP_STORE("Apple App Store"),
    ARC("Arc"),
    BATTLE_NET("Battle.net"),
    BETHESDA("Bethesda"),
    DIRECT_DOWNLOAD("Direct Download"),
    DISCORD("Discord"),
    EPIC_GAMES_DIGITAL("Epic Games (Digital)"),
    GAME_CENTER("GameCenter"),
    GAME_JOLT("Game Jolt"),
    GOG_DIGITAL("GOG (Digital)"),
    GOOGLE_PLAY("Google Play"),
    GOOGLE_STADIA("Google Stadia"),
    HUMBLE_BUNDLE("Humble Bundle"),
    INDIEGALA("IndieGala"),
    ITCH_IO("itch.io"),
    JAST_USA("JAST USA"),
    KARTRIDGE("Kartridge"),
    LEGACY_GAMES("Legacy Games"),
    MICROSOFT_STORE("Microsoft Store"),
    OCULUS("Oculus"),
    ORIGIN("Origin"),
    PARADOX_GAMES("Paradox Games"),
    PLAYSTATION_STORE("PlayStation Store"),
    ROBOT_CACHE("Robot Cache"),
    ROCKSTAR_GAMES("Rockstar Games"),
    STEAM_DIGITAL("Steam (Digital)"),
    UBISOFT_CONNECT("Ubisoft Connect"),
    XBOX_STORE("Xbox Store"),

    // Physical
    PHYSICAL_BORROWED("Physical (Borrowed)"),
    PHYSICAL_OWNED("Physical (Owned)"),
    PHYSICAL_RENTED("Physical (Rented)"),

    // Subscription
    ANTSTREAM_ARCADE("Antstream Arcade"),
    APPLE_ARCADE("Apple Arcade"),
    EA_PLAY("EA Play"),
    GOOGLE_PLAY_PASS("Google Play Pass"),
    GOOGLE_STADIA_PRO("Google Stadia Pro"),
    NETFLIX("Netflix"),
    NINTENDO_ONLINE_SUB("Nintendo Online (Subscription)"),
    PLAYSTATION_NOW("PlayStation Now"),
    PLAYSTATION_PLUS("PlayStation Plus"),
    UBISOFT_PLUS("Ubisoft+"),
    VIVEPORT("Viveport"),
    XBOX_GAME_PASS_SUB("Xbox Game Pass (Subscription)"),
    XBOX_GAMES_WITH_GOLD("Xbox Games w/ Gold");

    companion object {
        fun fromValue(value: String): Storefront? {
            return values().find { it.value == value }
        }

        val all: List<Storefront> = values().toList()
        val allWithNoneOption = listOf(MainActivity.context.getString(R.string.none)).plus(Storefront.all.map { it.value })
    }
}