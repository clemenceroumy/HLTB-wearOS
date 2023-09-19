package com.croumy.hltb_wearos.presentation.ui.addgame

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import androidx.wear.compose.material.SwipeToDismissValue
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberPickerState
import com.croumy.hltb_wearos.presentation.LocalNavController
import com.croumy.hltb_wearos.presentation.LocalNavSwipeBox
import com.croumy.hltb_wearos.presentation.models.Constants
import com.croumy.hltb_wearos.presentation.models.Storefront
import com.croumy.hltb_wearos.presentation.models.api.Category
import com.croumy.hltb_wearos.presentation.models.api.GameInfo
import com.croumy.hltb_wearos.presentation.models.api.categories
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.primary
import com.croumy.hltb_wearos.presentation.ui.addgame.components.AddGameButtons
import com.croumy.hltb_wearos.presentation.ui.addgame.models.AddGameStep
import com.croumy.hltb_wearos.presentation.ui.addgame.models.AddGameStep.Companion.isNext

@Composable
fun AddGameScreen(
    modifier: Modifier = Modifier,
    viewModel: AddGameViewModel = hiltViewModel(),
    game: GameInfo,
    navigateBack: () -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val navController = LocalNavController.current
    val swipeBoxState = LocalNavSwipeBox.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // 3 PICKER STATES TO WORK WITH ANIMATION
    val platformPickerState = rememberPickerState(
        initialNumberOfOptions = game.platformsWithNoneOption.size,
        initiallySelectedOption = 0,
        repeatItems = false
    )
    val storefrontPickerState = rememberPickerState(
        initialNumberOfOptions = Storefront.allWithNoneOption.size,
        initiallySelectedOption = 0,
        repeatItems = false
    )
    val categoryPickerState = rememberPickerState(
        initialNumberOfOptions = categories.size,
        initiallySelectedOption = categories.map { it.label!! }.indexOf(Category.Backlog.label),
        repeatItems = false
    )

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) { viewModel.init(game) }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(swipeBoxState.targetValue) {
        // ON BACK SWIPE, TELL SEARCH SCREEN TO NOT REDO A SEARCH QUERY
        if (swipeBoxState.targetValue == SwipeToDismissValue.Dismissed) {
            navController.previousBackStackEntry?.savedStateHandle?.apply {
                set(Constants.SEARCH_NEED_REFRESH, false)
            }
        }
    }

    LaunchedEffect(viewModel.isAdding.value) {
        if (viewModel.isAdding.value == false) {
            // AFTER ADDING GAME, TELL SEARCH SCREEN TO REDO A SEARCH QUERY TO REFRESH DATA
            navController.previousBackStackEntry?.savedStateHandle?.apply { set(Constants.SEARCH_NEED_REFRESH, true) }
            navigateBack()
        }
    }

    LaunchedEffect(platformPickerState.selectedOption) {
        viewModel.selectedPlatform.value = game.platformsWithNoneOption[platformPickerState.selectedOption]
    }
    LaunchedEffect(storefrontPickerState.selectedOption) {
        viewModel.selectedStorefront.value = Storefront.allWithNoneOption[storefrontPickerState.selectedOption]
    }
    LaunchedEffect(categoryPickerState.selectedOption) {
        viewModel.selectedCategory.value = categories.map { it.label!! }[categoryPickerState.selectedOption]
    }

    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = Dimensions.xsPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                game.game_name,
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = Dimensions.xsPadding)
            )
            AnimatedContent(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                targetState = viewModel.currentStep.value,
                transitionSpec = {
                    val isScrollingForward = this.targetState.isNext(this.initialState)

                    slideInHorizontally(animationSpec = tween(150, easing = FastOutLinearInEasing), initialOffsetX = {
                        if (isScrollingForward) screenWidth.value.toInt()
                        else -screenWidth.value.toInt()
                    }) togetherWith ExitTransition.None
                },
                contentKey = { it },
                label = ""
            ) { currentStep ->
                val state = when (currentStep) {
                    AddGameStep.PLATFORM -> platformPickerState
                    AddGameStep.STOREFRONT -> storefrontPickerState
                    AddGameStep.CATEGORY -> categoryPickerState
                }
                val pickerItems: List<String> = when (currentStep) {
                    AddGameStep.PLATFORM -> game.platformsWithNoneOption
                    AddGameStep.STOREFRONT -> Storefront.allWithNoneOption
                    AddGameStep.CATEGORY -> categories.map { it.label!! }
                }

                Picker(
                    modifier = Modifier.fillMaxWidth(),
                    state = state,
                    contentDescription = "",
                ) { index ->
                    Box(
                        modifier = if (index == state.selectedOption) Modifier
                            .defaultMinSize(minWidth = screenWidth / 2)
                            .background(
                                color = if (currentStep == AddGameStep.CATEGORY) Category.fromLabel(pickerItems[index]).color
                                else primary,
                                shape = CircleShape
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
            }
            AddGameButtons(
                modifier = Modifier.padding(horizontal = Dimensions.xsPadding),
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
                        AddGameStep.PLATFORM -> {
                            // ON CANCEL, TELL SEARCH SCREEN TO NOT REDO A SEARCH QUERY
                            navController.previousBackStackEntry?.savedStateHandle?.apply { set(Constants.SEARCH_NEED_REFRESH, false) }
                            navigateBack()
                        }

                        AddGameStep.STOREFRONT -> viewModel.currentStep.value = AddGameStep.PLATFORM
                        AddGameStep.CATEGORY -> viewModel.currentStep.value = AddGameStep.STOREFRONT
                    }
                }
            )
        }
    }
}