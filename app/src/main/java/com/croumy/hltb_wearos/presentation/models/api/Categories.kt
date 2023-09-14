package com.croumy.hltb_wearos.presentation.models.api

import androidx.compose.ui.graphics.Color

abstract class Category {
    abstract val value: String
    abstract val label: String?
    abstract val color: Color

    object Playing: Category() {
        override val value: String = "playing"
        override var label: String = "Playing"
        override val color: Color = Color(0xFF26762E)
    }

    object Backlog: Category() {
        override val value: String = "backlog"
        override var label: String = "Backlog"
        override val color: Color = Color(0xFF20669B)
    }

    object Custom: Category() {
        override val value: String = "custom"
        override var label: String? = "Paused"
        override val color: Color = Color(0xFF175A53)
    }

    object Custom2: Category() {
        override val value: String = "custom2"
        override var label: String? = "Stopped"
        override val color: Color = Color(0xFF175A53)
    }

    object Custom3: Category() {
        override val value: String = "custom3"
        override var label: String? = "Let's Play"
        override val color: Color = Color(0xFF175A53)
    }

    object Completed: Category() {
        override val value: String = "completed"
        override var label: String = "Completed"
        override val color: Color = Color(0xFF753C76)
    }

    object Retired: Category() {
        override val value: String = "retired"
        override var label: String = "Retired"
        override val color: Color = Color(0xFF9F2A2B)
    }

    companion object {
        val all = listOf(Playing, Backlog, Custom, Custom2, Custom3, Completed, Retired).filter { it.label != null && it.label?.isNotEmpty() == true }
    }
}