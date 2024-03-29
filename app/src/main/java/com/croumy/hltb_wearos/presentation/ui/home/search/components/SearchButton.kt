package com.croumy.hltb_wearos.presentation.ui.home.search.components

import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material.Icon
import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.input.wearableExtender
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.primary
import com.croumy.hltbwearos.R

@Composable
fun SearchButton(
    onSearchResult: (String) -> Unit
) {
    val context = LocalContext.current
    val inputTextKey = "input_text_key"
    val remoteInputs: List<RemoteInput> = listOf(
        RemoteInput.Builder(inputTextKey)
            .setLabel(stringResource(id = R.string.search_title))
            .setChoices(arrayOf(stringResource(id = R.string.search_clear_input)))
            .wearableExtender {
                setEmojisAllowed(false)
                setInputActionType(EditorInfo.IME_ACTION_SEARCH)
            }
            .build()
    )
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        it.data?.let { data ->
            val searchValue: String? = RemoteInput.getResultsFromIntent(data)?.getString(inputTextKey)
            if(searchValue == context.getString(R.string.search_clear_input)) {
                // IF CHOICE BTN CLICK (CLEAR SEARCH)
                onSearchResult("")
            } else {
                onSearchResult(searchValue ?: "")
            }
        }
    }
    val intent: Intent = RemoteInputIntentHelper.createActionRemoteInputIntent()
    RemoteInputIntentHelper.putRemoteInputsExtra(intent, remoteInputs)

    Box(
        modifier = Modifier
            .background(primary, CircleShape)
            .clickable { launcher.launch(intent) }
            .padding(horizontal = Dimensions.sPadding, vertical = Dimensions.xxsPadding),
    ) {
        Icon(Icons.Rounded.Search, "search game")
    }
}