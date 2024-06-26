package com.croumy.hltb_wearos.presentation.ui.addgame.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateBefore
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close

import androidx.compose.ui.graphics.vector.ImageVector

enum class AddGameStep {
    PLATFORM {
        override val actionIcon: ImageVector = Icons.AutoMirrored.Rounded.NavigateNext
        override val secondaryActionIcon: ImageVector = Icons.Rounded.Close
    },
    STOREFRONT {
        override val actionIcon: ImageVector = Icons.AutoMirrored.Rounded.NavigateNext
        override val secondaryActionIcon: ImageVector = Icons.AutoMirrored.Rounded.NavigateBefore
    },
    CATEGORY {
        override val actionIcon: ImageVector = Icons.Rounded.Check
        override val secondaryActionIcon: ImageVector = Icons.AutoMirrored.Rounded.NavigateBefore
    };

    abstract val actionIcon: ImageVector
    abstract val secondaryActionIcon: ImageVector

    companion object {
        fun AddGameStep.isNext(currentStep: AddGameStep): Boolean = currentStep.ordinal < this.ordinal
    }
}