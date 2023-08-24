package com.croumy.hltbwearos.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.croumy.hltbwearos.R
import com.croumy.hltbwearos.components.DotsPulsing
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.material.SetupMaterialRichText
import com.halilibo.richtext.ui.string.RichTextStringStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    navigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = { TopAppBar(modifier = Modifier.height(0.dp), title = {}) }
    ) { paddings ->
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .padding(paddings)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(vertical = 10.dp)
                    .padding(start = 10.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { navigateBack() }) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(id = R.string.app_name), style = MaterialTheme.typography.titleMedium)
            }

            Column(
                Modifier
                    .weight(1f)
                    .padding(vertical = 40.dp, horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                    RichText(
                        style = RichTextStyle(
                            stringStyle = RichTextStringStyle(
                                codeStyle = SpanStyle(
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                )
                            ),
                        )
                    ) {
                        Markdown(stringResource(id = R.string.info_description))
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(stringResource(id = R.string.info_legal_title), style = MaterialTheme.typography.titleMedium)
                        Markdown(stringResource(id = R.string.info_legal))
                        Spacer(modifier = Modifier.height(20.dp))
                        Markdown(stringResource(id = R.string.info_app))
                    }
            }
        }
    }
}