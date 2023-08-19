package com.croumy.hltbwearos

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state = rememberWebViewState("https://howlongtobeat.com/login")

            suspend fun sendCookie(token: String) {
                val nodes = Wearable.getNodeClient(this@MainActivity).connectedNodes.await()

                Wearable.getMessageClient(this).sendMessage(
                    nodes[0].id,
                    "/hltb_token",
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

            val webViewClient: AccompanistWebViewClient = object : AccompanistWebViewClient() {
                override fun doUpdateVisitedHistory(
                    view: WebView?,
                    url: String?,
                    isReload: Boolean
                ) {
                    super.doUpdateVisitedHistory(view, url, isReload)

                    val cookie = CookieManager.getInstance().getCookie(url)
                    val hltbToken = Helper.getCookieValueByKey(cookie, "hltb_alive")

                    if (cookie?.isNotEmpty() == true && hltbToken.isNotEmpty()) {
                        lifecycleScope.launch { sendCookie(hltbToken) }
                    }
                }
            }

            WebView(
                state,
                modifier = Modifier,
                client = webViewClient,
                onCreated = {
                    it.settings.javaScriptEnabled = true
                },
            )
        }
    }
}