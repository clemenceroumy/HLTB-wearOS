package com.croumy.hltbwearos

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.lifecycleScope
import com.croumy.hltbwearos.components.DotsPulsing
import com.croumy.hltbwearos.ui.theme.HLTBwearosTheme
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class MainActivity : ComponentActivity(), MessageClient.OnMessageReceivedListener {
    val isSyncing = mutableStateOf(false)
    val isDone = mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state = rememberWebViewState("https://howlongtobeat.com/login")

            suspend fun sendCookie(token: String) {
                val nodes = Wearable.getNodeClient(this@MainActivity).connectedNodes.await()

                nodes.forEach { node ->
                    Wearable.getMessageClient(this).sendMessage(
                        node.id,
                        Constants.DATA_LAYER_TOKEN_CHANNEL,
                        token.toByteArray()
                    ).apply {
                        addOnSuccessListener {
                            Log.i("MainActivity", "sendCookie: $token")
                        }
                        addOnFailureListener {
                            Log.i("MainActivity", "sendCookie error: $it")
                        }
                    }
                }
            }

            val webViewClient: AccompanistWebViewClient = object : AccompanistWebViewClient() {
                override fun doUpdateVisitedHistory(
                    view: WebView?,
                    url: String?,
                    isReload: Boolean
                ) {
                    super.doUpdateVisitedHistory(view, url, isReload)

                    val cookie = CookieManager.getInstance().getCookie(url)
                    val hltbToken = Uri.decode(Helper.getCookieValueByKey(cookie, "hltb_alive"))

                    if (cookie?.isNotEmpty() == true && hltbToken.isNotEmpty()) {
                        lifecycleScope.launch {
                            isSyncing.value = true
                            sendCookie(hltbToken)
                        }
                    }
                }
            }

            HLTBwearosTheme {
                if(isDone.value) {
                    AlertDialog(
                        onDismissRequest = { /*TODO*/ },
                        icon = { Icon(Icons.Filled.Done, contentDescription = null, tint = MaterialTheme.colorScheme.secondary) },
                        title = { Text(stringResource(id = R.string.dialog_syncing_done_title)) },
                        text = { Text(stringResource(id = R.string.dialog_syncing_done_description), textAlign = TextAlign.Center) },
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.secondary,
                        textContentColor = MaterialTheme.colorScheme.onSurface,
                        confirmButton = {
                            TextButton(
                                onClick = { finishAffinity() }
                            ) {
                                Text(text = stringResource(id = R.string.close_app),)
                            }
                        }
                    )
                } else if(isSyncing.value) {
                    AlertDialog(
                        onDismissRequest = { /*TODO*/ },
                        icon = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Outlined.PhoneAndroid, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                                Spacer(modifier = Modifier.width(5.dp))
                                DotsPulsing()
                                Spacer(modifier = Modifier.width(5.dp))
                                Icon(Icons.Outlined.Watch, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                            } },
                        title = { Text(stringResource(id = R.string.dialog_syncing_title)) },
                        text = { Text(stringResource(id = R.string.dialog_syncing_description), textAlign = TextAlign.Center) },
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.secondary,
                        textContentColor = MaterialTheme.colorScheme.onSurface,
                        confirmButton = {}
                    )
                }

                Scaffold(
                    topBar = { TopAppBar(modifier = Modifier.height(0.dp), title = {}) }
                ) { paddings ->
                    Column(
                        Modifier.fillMaxSize()
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.tertiary)
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                                .padding(bottom = 5.dp)
                        ) {
                            Text(stringResource(id = R.string.app_name), style = MaterialTheme.typography.titleMedium)
                            Text(stringResource(id = R.string.app_description), style = MaterialTheme.typography.bodySmall)
                        }

                        WebView(
                            state,
                            modifier = Modifier
                                .padding(paddings)
                                .weight(1f),
                            client = webViewClient,
                            onCreated = {
                                var started = false

                                it.settings.javaScriptEnabled = true
                                it.setOnScrollChangeListener { _, _, _, _, _ ->
                                    if (!started && it.scrollY > 0) {
                                        it.scrollY = 0
                                        started = true
                                    }
                                }
                            },
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Wearable.getMessageClient(this).addListener(this)
    }

    override fun onPause() {
        super.onPause()
        Wearable.getMessageClient(this).removeListener(this)
    }

    override fun onMessageReceived(message: MessageEvent) {
        if(message.path == Constants.DATA_LAYER_DATA_RECEIVED) {
            isSyncing.value = false
            isDone.value = true
        }
    }
}