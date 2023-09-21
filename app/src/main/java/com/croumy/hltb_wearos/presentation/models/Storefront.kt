package com.croumy.hltb_wearos.presentation.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import com.croumy.hltb_wearos.presentation.MainActivity
import com.croumy.hltbwearos.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.Delegates

// NOT FOUND API CALL FROM HLTB WEBSITE SO I CREATED AN HARDCODED ENUM CLASS
enum class Storefront(val value: String) {
    // Digital
    AMAZON_GAME_APP("Amazon Game App"),
    AMAZON_LUNA("Amazon Luna"),
    APPLE_APP_STORE("Apple App Store"),
    ARC("Arc"),
    BATTLE_NET("Battle.net"),
    BETHESDA("Bethesda"),
    DIRECT_DOWNLOAD("Direct Download"),
    DISCORD("Discord"),
    EPIC_GAMES("Epic Games"),
    GAME_CENTER("GameCenter"),
    GAME_JOLT("Game Jolt"),
    GOG("GOG"),
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
    STEAM("Steam"),
    UBISOFT_CONNECT("Ubisoft Connect"),
    XBOX_STORE("Xbox Store"),

    // Physical
    RENTED("Rented"),
    BORROWED("Borrowed"),
    PHYSICAL("Physical"),

    // Subscription
    ANTSTREAM_ARCADE("Antstream Arcade"),
    APPLE_ARCADE("Apple Arcade"),
    EA_PLAY("EA Play"),
    GOOGLE_PLAY_PASS("Google Play Pass"),
    GOOGLE_STADIA_PRO("Google Stadia Pro"),
    NETFLIX("Netflix"),
    NINTENDO_ONLINE("Nintendo Online"),
    PLAYSTATION_NOW("PlayStation Now"),
    PLAYSTATION_PLUS("PlayStation Plus"),
    UBISOFT_PLUS("Ubisoft+"),
    VIVEPORT("Viveport"),
    XBOX_GAME_PASS("Xbox Game Pass"),
    XBOX_GAMES_WITH_GOLD("Xbox Games w/ Gold");

    companion object {
        fun fromValue(value: String): Storefront? {
            return values().find { it.value == value }
        }

        var all: List<Storefront> = values().toList()
        lateinit var allWithNoneOption: List<String>
    }
}