package com.croumy.hltb_wearos.presentation.ui.home.search

import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.input.wearableExtender
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.primary
import com.croumy.hltbwearos.R

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    modifier: Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val inputTextKey = "input_text_key"
    val remoteInputs: List<RemoteInput> = listOf(
        RemoteInput.Builder(inputTextKey)
            .setLabel(stringResource(id = R.string.search_title))
            .wearableExtender {
                setEmojisAllowed(false)
                setInputActionType(EditorInfo.IME_ACTION_NEXT)
            }
            .build()
    )
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        it.data?.let { data ->
            val results: Bundle? = RemoteInput.getResultsFromIntent(data)
            val newInputText: CharSequence? = results?.getCharSequence(inputTextKey)
            viewModel.searchText.value = newInputText?.toString() ?: ""
        }
    }
    val intent: Intent = RemoteInputIntentHelper.createActionRemoteInputIntent()
    RemoteInputIntentHelper.putRemoteInputsExtra(intent, remoteInputs)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                //launcher.launch(intent)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(viewModel.searchText.value) {
        if(viewModel.searchText.value.isNotEmpty()) {
            viewModel.search()
        }
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
    ) {
        if (viewModel.searchText.value.isNotEmpty()) {
            if(viewModel.isSearching.value) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = Dimensions.xsStrokeSize,
                    modifier = Modifier
                        .size(Dimensions.mSize)
                        .align(Alignment.Center)
                )
            } else ScalingLazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                itemsIndexed(viewModel.resultGames.value) { index, game ->
                    Text(text = game.game_name)
                    Spacer(modifier = Modifier.height(Dimensions.xsPadding))
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(id = R.string.search_title),
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(Dimensions.xsPadding))
                IconButton(
                    onClick = { launcher.launch(intent) },
                    modifier = Modifier.background(primary, CircleShape)
                ) {
                    Icon(Icons.Rounded.Search, "search game")
                }
            }
        }
    }
}