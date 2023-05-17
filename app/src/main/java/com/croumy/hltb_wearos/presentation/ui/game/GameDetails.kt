package com.croumy.hltb_wearos.presentation.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.croumy.hltb_wearos.presentation.helpers.asString
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.ui.game.components.LaunchButtons
import com.croumy.hltb_wearos.presentation.ui.game.components.LoadingGame

@Composable
fun GameDetails(
    viewModel: GameViewModel = hiltViewModel()
) {
    val game = viewModel.game.value

    Column(
        Modifier.fillMaxSize().padding(vertical = Dimensions.sPadding, horizontal = Dimensions.xxsPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (game == null && !viewModel.isLoading.value) {
            LoadingGame()
        } else if (game == null) {
            Text("Can't retrieve game")
        } else {
            Text(game.platform, style = MaterialTheme.typography.body2)
            Spacer(Modifier.width(Dimensions.xsPadding))
            Text(
                game.custom_title,
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.width(Dimensions.xsPadding))
            Text(game.timePlayed.asString(), style = MaterialTheme.typography.title1)
            Spacer(Modifier.width(Dimensions.xsPadding))
            LaunchButtons()
        }
    }
}