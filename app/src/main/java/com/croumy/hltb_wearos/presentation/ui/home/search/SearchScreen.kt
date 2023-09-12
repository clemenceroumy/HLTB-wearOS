package com.croumy.hltb_wearos.presentation.ui.home.search

import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.input.wearableExtender
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.primary
import com.croumy.hltb_wearos.presentation.ui.components.SearchGameItem
import com.croumy.hltb_wearos.presentation.ui.home.search.components.SearchButton
import com.croumy.hltbwearos.R
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    listState: ScalingLazyListState,
    focusRequester: FocusRequester,
    modifier: Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel.searchText.value) {
        viewModel.search()
    }

    LaunchedEffect(viewModel.resultGames.value) {
        if(viewModel.resultGames.value.isNotEmpty()) {
            focusRequester.requestFocus()
        }
    }

    Box(
        modifier = modifier.fillMaxHeight()
    ) {
        if (viewModel.isSearching.value) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = Dimensions.xsStrokeSize,
                modifier = Modifier
                    .padding(Dimensions.xsPadding)
                    .size(Dimensions.sIcon)
                    .align(Alignment.Center)
            )
        } else Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SearchButton(onSearchResult = { viewModel.searchText.value = it })
            Spacer(Modifier.height(Dimensions.xsPadding))
            ScalingLazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .onRotaryScrollEvent {
                        coroutineScope.launch {
                            listState.scrollBy(it.verticalScrollPixels)
                        }
                        true
                    }
                    .focusRequester(focusRequester)
                    .focusable(),
                state = listState,
            ) {
                itemsIndexed(viewModel.resultGames.value) { index, game ->
                    SearchGameItem(game)
                }
            }
        }
    }
}