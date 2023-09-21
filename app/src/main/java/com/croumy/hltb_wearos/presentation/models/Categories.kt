package com.croumy.hltb_wearos.presentation.models

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
        override var label: String? = ""
        override val color: Color = Color(0xFF175A53)

        fun updateLabel(newLabel: String?) {
            label = newLabel
        }
    }

    object Custom2: Category() {
        override val value: String = "custom2"
        override var label: String? = ""
        override val color: Color = Color(0xFF175A53)

        fun updateLabel(newLabel: String?) {
            label = newLabel
        }
    }

    object Custom3: Category() {
        override val value: String = "custom3"
        override var label: String? = ""
        override val color: Color = Color(0xFF175A53)

        fun updateLabel(newLabel: String?) {
            label = newLabel
        }
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
        fun fromLabel(label: String): Category {
            return when(label) {
                Playing.label -> Playing
                Backlog.label -> Backlog
                Custom.label -> Custom
                Custom2.label -> Custom2
                Custom3.label -> Custom3
                Completed.label -> Completed
                Retired.label -> Retired
                else -> Backlog
            }
        }
    }
}
val categories = listOf(Category.Playing, Category.Backlog, Category.Custom, Category.Custom2, Category.Custom3, Category.Completed, Category.Retired)
    .filter { it.label != null && it.label?.isNotEmpty() == true }