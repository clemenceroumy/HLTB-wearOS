package com.croumy.hltb_wearos.presentation.models.api

enum class Categories {
    PLAYING {
        override val value: String = "playing"
    },
    BACKLOG {
        override val value: String = "backlog"
    },
    CUSTOM {
        override val value: String = "custom"
    },
    CUSTOM2 {
        override val value: String = "custom2"
    },
    CUSTOM3 {
        override val value: String = "custom3"
    },
    COMPLETED {
        override val value: String = "completed"
    },
    RETIRED {
        override val value: String = "retired"
    };

    abstract val value: String
}