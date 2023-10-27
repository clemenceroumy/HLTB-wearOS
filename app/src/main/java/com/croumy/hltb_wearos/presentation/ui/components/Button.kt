package com.croumy.hltb_wearos.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.croumy.hltb_wearos.presentation.theme.Dimensions

@Composable
fun Button(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.surface, CircleShape)
            .clickable { onClick() }
            .padding(vertical = Dimensions.xsPadding, horizontal = Dimensions.sPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = "",
            Modifier.size(15.dp)
        )
        Spacer(Modifier.width(Dimensions.xxsPadding))
        Text(text = text, style = MaterialTheme.typography.body2)
    }
}