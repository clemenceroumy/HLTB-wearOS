package com.croumy.hltb_wearos.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import coil.compose.AsyncImage
import com.croumy.hltb_wearos.presentation.models.api.Game
import com.croumy.hltb_wearos.presentation.theme.Dimensions

@Composable
fun GameItem(game: Game) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.lSize)
            .background(MaterialTheme.colors.surface, CircleShape)
            .padding(horizontal = Dimensions.xsPadding, vertical = Dimensions.xxsPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = game.picture,
            contentDescription = game.custom_title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(Dimensions.mIcon)
                .clip(CircleShape)
        )
        Spacer(Modifier.width(Dimensions.xsPadding))
        Column(
            Modifier.fillMaxHeight().padding(vertical = Dimensions.xxsPadding),
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