package com.croumy.hltb_wearos.presentation.ui.home.addgame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberPickerState
import com.croumy.hltb_wearos.presentation.models.Storefront
import com.croumy.hltb_wearos.presentation.models.api.Category
import com.croumy.hltb_wearos.presentation.models.api.GameInfo
import com.croumy.hltb_wearos.presentation.models.api.categories
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.primary
import com.croumy.hltb_wearos.presentation.ui.home.addgame.components.AddGameButtons
import com.croumy.hltb_wearos.presentation.ui.home.addgame.models.AddGameStep
import com.croumy.hltbwearos.R

@Composable
fun AddGameScreen(
    viewModel: AddGameViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    game: GameInfo,
    navigateBack: () -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val pickerItems: List<String> = when (viewModel.currentStep.value) {
        AddGameStep.PLATFORM -> game.platformsWithNoneOption
        AddGameStep.STOREFRONT -> Storefront.allWithNoneOption
        AddGameStep.CATEGORY -> categories.map { it.label!! }
    }
    val pickerState = rememberPickerState(
        initialNumberOfOptions = pickerItems.size,
        initiallySelectedOption = when (viewModel.currentStep.value) {
            AddGameStep.PLATFORM -> 0
            AddGameStep.STOREFRONT -> 0
            AddGameStep.CATEGORY -> pickerItems.indexOf(Category.Backlog.label)
        },
        repeatItems = false
    )

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.init(game)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(viewModel.isAdding.value) {
        if (viewModel.isAdding.value == false) { navigateBack() }
    }

    LaunchedEffect(pickerState.selectedOption) {
        when (viewModel.currentStep.value) {
            AddGameStep.PLATFORM -> viewModel.selectedPlatform.value = pickerItems[pickerState.selectedOption]
            AddGameStep.STOREFRONT -> viewModel.selectedStorefront.value = pickerItems[pickerState.selectedOption]
            AddGameStep.CATEGORY -> viewModel.selectedCategory.value = pickerItems[pickerState.selectedOption]
        }
    }

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
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Picker(
                modifier = Modifier.weight(1f),
                state = pickerState,
                contentDescription = "",
            ) { index ->
                Box(
                    modifier = if (index == pickerState.selectedOption) Modifier
                        .defaultMinSize(minWidth = screenWidth / 2)
                        .background(
                            color = if (viewModel.currentStep.value == AddGameStep.CATEGORY) Category.fromLabel(pickerItems[index]).color
                            else primary,
                            CircleShape
                        )
                        .padding(vertical = Dimensions.xsPadding, horizontal = Dimensions.sPadding)
                    else Modifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = pickerItems[index],
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                    )
                }
            }
            AddGameButtons(
                currentStep = viewModel.currentStep.value,
                isLoading = viewModel.isAdding.value == true,
                onAction = {
                    when (viewModel.currentStep.value) {
                        AddGameStep.PLATFORM -> viewModel.currentStep.value = AddGameStep.STOREFRONT
                        AddGameStep.STOREFRONT -> viewModel.currentStep.value = AddGameStep.CATEGORY
                        AddGameStep.CATEGORY -> viewModel.addGame()
                    }
                },
                onSecondaryAction = {
                    when (viewModel.currentStep.value) {
                        AddGameStep.PLATFORM -> navigateBack()
                        AddGameStep.STOREFRONT -> viewModel.currentStep.value = AddGameStep.PLATFORM
                        AddGameStep.CATEGORY -> viewModel.currentStep.value = AddGameStep.STOREFRONT
                    }
                }
            )
        }
    }
}