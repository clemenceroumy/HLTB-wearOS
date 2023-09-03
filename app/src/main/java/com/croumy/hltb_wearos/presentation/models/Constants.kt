package com.croumy.hltb_wearos.presentation.models

class Constants {
    companion object {
        const val CHANNEL_ID = "TimerChannel"
        const val NOTIFICATION_ID = 1

        const val DEEPLINK_PHONE = "app://com.croumy.hltbwearos"
        const val GITHUB_REPO = "https://github.com/clemenceroumy/HLTB-wearOS/releases"
        const val PHONE_CAPABILITY = "verify_remote_example_phone_app"

        const val DATA_LAYER_TOKEN_CHANNEL = "/hltb_token"
        const val DATA_LAYER_DATA_RECEIVED = "/data_received"

        const val PREFERENCES = "com.croumy.hltbwearos_preferences"
        const val PREFERENCES_TOKEN = "hltb_token"
        const val PREFERENCES_USER_ID = "user_id"

        const val HOME_NEED_REFRESH = "needRefresh"
        const val HOME_PREVIOUS_GAME_ID = "previousGameId"
    }
}