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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.wear.compose.material.MaterialTheme
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.primary
import com.croumy.hltb_wearos.presentation.ui.home.addgame.models.AddGameStep

@Composable
fun AddGameButtons(
    modifier: Modifier = Modifier,
    currentStep: AddGameStep,
    isLoading: Boolean,
    onAction: () -> Unit = {},
    onSecondaryAction: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .size(Dimensions.mIcon)
                .background(MaterialTheme.colors.surface, CircleShape)
                .clip(CircleShape)
                .clickable { onSecondaryAction() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                currentStep.secondaryActionIcon,
                contentDescription = "",
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
                .clickable { if(!isLoading) onAction() },
            contentAlignment = Alignment.Center
        ) {
            if (isLoading)
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(Dimensions.xsPadding)
                        .size(Dimensions.sIcon),
                    color = Color.White,
                    strokeWidth = Dimensions.xsStrokeSize,
                    strokeCap = StrokeCap.Round
                )
            else Icon(
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