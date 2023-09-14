package com.croumy.hltb_wearos.presentation.ui.home.addgame.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.MaterialTheme
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.primary
import com.croumy.hltb_wearos.presentation.ui.home.addgame.models.AddGameStep

@Composable
fun AddGameButtons(
    currentStep: AddGameStep,
    onAction: () -> Unit = {},
    navigateBack: () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .size(Dimensions.mIcon)
                .background(MaterialTheme.colors.surface, CircleShape)
                .clip(CircleShape)
                .clickable { navigateBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = "Cancel",
                modifier = Modifier
                    .padding(Dimensions.xxsPadding)
                    .size(Dimensions.mIcon),
                tint = Color.White
            )
        }
        Spacer(Modifier.width(Dimensions.sPadding))
        Box(
            Modifier
                .size(Dimensions.mIcon)
                .background(primary, CircleShape)
                .clip(CircleShape)
                .clickable { onAction() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                currentStep.actionIcon,
                contentDescription = "",
                modifier = Modifier
                    .padding(Dimensions.xxsPadding)
                    .size(Dimensions.mIcon),
                tint = Color.White
            )
        }
    }
}