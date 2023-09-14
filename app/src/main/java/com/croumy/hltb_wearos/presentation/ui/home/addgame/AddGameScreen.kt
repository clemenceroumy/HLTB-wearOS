package com.croumy.hltb_wearos.presentation.ui.home.addgame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberPickerState
import com.croumy.hltb_wearos.presentation.models.api.GameInfo
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.primary
import com.croumy.hltb_wearos.presentation.ui.home.addgame.components.AddGameButtons
import com.croumy.hltbwearos.R

@Composable
fun AddGameScreen(
    viewModel: AddGameViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    game: GameInfo,
    navigateBack: () -> Unit = {},
) {
    val pickerItems =  game.platforms
    val pickerState = rememberPickerState(
        initialNumberOfOptions = pickerItems.size,
        initiallySelectedOption = 0,
        repeatItems = false
    )

    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(Dimensions.xsPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                game.game_name,
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Text(stringResource(id = R.string.add_game_platform))
            Picker(
                state = pickerState,
                contentDescription = "",
                Modifier.weight(1f)
            ) { index ->
                Text(
                    text = pickerItems[index],
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.title2.copy(fontWeight = FontWeight.Normal),
                    modifier = if(index == pickerState.selectedOption) Modifier
                        .background(primary, CircleShape)
                        .padding(Dimensions.xsPadding)
                    else Modifier
                )
            }
            AddGameButtons(
                currentStep = viewModel.currentStep.value,
                onAction = {

                },
                navigateBack = navigateBack
            )
        }
    }
}