package com.croumy.hltb_wearos.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.material.icons.rounded.Check
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
import com.croumy.hltb_wearos.presentation.models.api.GameInfo
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.primary
import com.croumy.hltb_wearos.presentation.theme.secondary

@Composable
fun SearchGameItem(
    game: GameInfo,
    modifier: Modifier = Modifier,
    onAddClick: (GameInfo) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .sizeIn(minHeight = Dimensions.lSize)
            .background(MaterialTheme.colors.surface, CircleShape)
            .clip(CircleShape)
            .clickable {
                if(!game.isInUserList) onAddClick(game)
            }
            .padding(horizontal = Dimensions.xsPadding, vertical = Dimensions.xxsPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = game.picture,
                contentDescription = game.game_name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(Dimensions.lIcon)
                    .clip(CircleShape)
            )
        }
        Spacer(Modifier.width(Dimensions.xsPadding))
        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(vertical = Dimensions.xxsPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(0.dp))
            Text(
                game.game_name,
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (game.isInUserList) Icon(
            Icons.Rounded.Check,

            contentDescription = "Game already in list",
            tint = secondary,
            modifier = Modifier.padding(start = Dimensions.xxsPadding)
        )
        else Icon(
            Icons.Rounded.Add,
            contentDescription = "Add to list",
            modifier = Modifier.padding(start = Dimensions.xxsPadding)
        )
    }
}