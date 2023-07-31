package com.croumy.hltb_wearos.presentation.ui.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.shimmerColor
import com.valentinilk.shimmer.shimmer

@Composable
fun LoadingGame() {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // LOADER FOR PLATFORM
        Box(
            modifier = Modifier
                .shimmer()
                .background(shimmerColor, CircleShape)
        ) { Text("Playstation Vita", color = Color.Transparent, style = MaterialTheme.typography.body2) }

        //LOADER FOR GAME NAME
        Box(
            modifier = Modifier
                .shimmer()
                .background(shimmerColor, CircleShape)
        ) { Text("Ghost of Tsushima", color = Color.Transparent, style = MaterialTheme.typography.body1) }

        //LOADER FOR TIME
        Box(
            modifier = Modifier
                .shimmer()
                .background(shimmerColor, CircleShape)
        ) { Text("24h50min", color = Color.Transparent, style = MaterialTheme.typography.title1) }

        Box(
            Modifier
                .shimmer()
                .size(Dimensions.mIcon)
                .background(shimmerColor, CircleShape)
        )
    }
}