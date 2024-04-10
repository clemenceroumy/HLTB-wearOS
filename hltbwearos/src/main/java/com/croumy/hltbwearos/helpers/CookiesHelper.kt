package com.croumy.hltbwearos.helpers

import android.content.Context
import android.util.Log
import com.croumy.hltbwearos.data.Constants
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await

class CookiesHelper {
    companion object {
        fun getCookieValueByKey(cookieString: String?, searchedKey: String): String {
            var value = ""
            val cookiesValues = cookieString?.split("; ")
            cookiesValues?.forEach { cookie ->
                val keyValue = cookie.split("=")
                if (keyValue.size == 2) {
                    val cookieKey = keyValue[0]
                    if (cookieKey == searchedKey) {
                        value = "$cookieKey=${keyValue[1]}"
                    }
                }
            }
            return value
        }

        suspend fun sendCookieToWatch(token: String, context: Context) {
            val nodes = Wearable.getNodeClient(context).connectedNodes.await()

            nodes.forEach { node ->
                Wearable.getMessageClient(context).sendMessage(
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
    }
}