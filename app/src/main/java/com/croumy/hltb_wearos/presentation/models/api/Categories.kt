package com.croumy.hltb_wearos.presentation.models.api

import androidx.compose.ui.graphics.Color

enum class Categories {
    PLAYING {
        override val value: String = "playing"
        override val label: String = "Playing"
        override val color: Color = Color(0xFF26762E)
    },
    BACKLOG {
        override val value: String = "backlog"
        override val label: String = "Backlog"
        override val color: Color = Color(0xFF20669B)
    },
    CUSTOM {
        override val value: String = "custom"
        override val label: String = "Paused"
        override val color: Color = Color(0xFF175A53)
    },
    CUSTOM2 {
        override val value: String = "custom2"
        override val label: String = "Stopped"
        override val color: Color = Color(0xFF175A53)
    },
    CUSTOM3 {
        override val value: String = "custom3"
        override val label: String = "Let's Play"
        override val color: Color = Color(0xFF175A53)
    },
    COMPLETED {
        override val value: String = "completed"
        override val label: String = "Completed"
        override val color: Color = Color(0xFF753C76)
    },
    RETIRED {
        override val value: String = "retired"
        override val label: String = "Retired"
        override val color: Color = Color(0xFF9F2A2B)
    };

    abstract val value: String
    abstract val label: String
    abstract val color: Color
}