package com.croumy.hltb_wearos.presentation.ui.startapp.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.SendToMobile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.croumy.hltb_wearos.presentation.components.RefreshButton
import com.croumy.hltb_wearos.presentation.helpers.Launcher
import com.croumy.hltb_wearos.presentation.models.Constants.Companion.GITHUB_REPO
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltbwearos.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PhoneAppNotFound(
    refresh: suspend () -> Unit
) {
    val context = LocalContext.current
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.xsPadding)
            .padding(top = Dimensions.mPadding, bottom = Dimensions.xsPadding),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.phone_app_needed),
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
        )

        Text(
            text = stringResource(id = R.string.phone_app_needed_description),
            textAlign = TextAlign.Center,
        )


        Row(
            modifier = Modifier
                .background(MaterialTheme.colors.surface, CircleShape)
                .clickable { lifecycleScope.launch { Launcher.launchRemoteActivity(GITHUB_REPO, context) } }
                .padding(vertical = Dimensions.xsPadding, horizontal = Dimensions.sPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.SendToMobile, contentDescription = "Send to mobile", Modifier.size(15.dp))
            Spacer(Modifier.width(Dimensions.xxsPadding))
            Text(text = stringResource(id = R.string.phone_app_needed_btn), style = MaterialTheme.typography.body2)
        }
        RefreshButton(refresh = refresh)
    }
}