package com.croumy.hltb_wearos.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import coil.compose.AsyncImage
import com.croumy.hltb_wearos.presentation.models.api.Game
import com.croumy.hltb_wearos.presentation.theme.Dimensions

@Composable
fun GameItem(game: Game, isRunning: Boolean, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimensions.lSize)
            .background(MaterialTheme.colors.surface, CircleShape)
            .clip(CircleShape)
            .padding(horizontal = Dimensions.xsPadding, vertical = Dimensions.xxsPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = game.picture,
                contentDescription = game.custom_title,
                contentScale = ContentScale.Crop,
                colorFilter = if(isRunning) ColorFilter.tint(Color.Black.copy(alpha = 0.6f), blendMode = BlendMode.Darken) else null,
                modifier = Modifier
                    .size(Dimensions.lIcon)
                    .clip(CircleShape)
            )
            if(isRunning) {
                Icon(Icons.Rounded.Alarm, contentDescription = "Timer is running", tint = Color.White, modifier = Modifier.size(Dimensions.xsIcon))
            }
        }
        Spacer(Modifier.width(Dimensions.xsPadding))
        Column(
            Modifier
                .fillMaxHeight()
                .padding(vertical = Dimensions.xxsPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(0.dp))
            Text(
                game.custom_title, 
                style = MaterialTheme.typography.body1.copy(fontWeight= FontWeight.Bold),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.width(Dimensions.xsPadding))
            Text(game.platform, style = MaterialTheme.typography.body2)
        }
    }
}