package com.croumy.hltbwearos

import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state = rememberWebViewState("https://howlongtobeat.com/login")

            val webViewClient: AccompanistWebViewClient = object : AccompanistWebViewClient() {
                override fun doUpdateVisitedHistory(
                    view: WebView?,
                    url: String?,
                    isReload: Boolean
                ) {
                    super.doUpdateVisitedHistory(view, url, isReload)
                    val cookie = CookieManager.getInstance().getCookie(url)

                    if(cookie?.isNotEmpty() == true) {
                        println("Cookie: $cookie")
                        val hltbToken = Helper.getCookieValueByKey(cookie, "hltb_alive")
                        // TODO: SEND VALUE TO WATCH APP AND CLOSE PHONE APP
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