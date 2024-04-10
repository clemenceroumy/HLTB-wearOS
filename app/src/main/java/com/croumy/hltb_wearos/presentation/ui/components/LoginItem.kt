package com.croumy.hltb_wearos.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.SendToMobile
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltbwearos.R

@Composable
fun LoginItem(
    onLogin: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(vertical = Dimensions.mPadding, horizontal = Dimensions.xsPadding),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.login_title),
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = stringResource(id = R.string.login_needed),
            textAlign = TextAlign.Center
        )
        Chip(
            label = { Text(text = stringResource(id = R.string.login_btn), style = MaterialTheme.typography.body2) },
            icon = { Icon(Icons.AutoMirrored.Rounded.SendToMobile, contentDescription = "Send to mobile") },
            colors = ChipDefaults.primaryChipColors(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface
            ),
            onClick = { onLogin() }
        )
    }
}