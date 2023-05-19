package com.croumy.hltb_wearos.presentation.ui.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import com.croumy.hltb_wearos.presentation.theme.Dimensions
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
                .fillMaxWidth()
                .height(MaterialTheme.typography.body2.fontSize.value.dp)
        )

        //LOADER FOR GAME NAME
        Box(
            modifier = Modifier
                .shimmer()
                .fillMaxWidth()
                .height(MaterialTheme.typography.body1.fontSize.value.dp)
        )

        //LOADER FOR TIME
        Box(
            modifier = Modifier
                .shimmer()
                .fillMaxWidth()
                .height(MaterialTheme.typography.title1.fontSize.value.dp)
        )

        Box(Modifier.size(0.dp))
    }
}