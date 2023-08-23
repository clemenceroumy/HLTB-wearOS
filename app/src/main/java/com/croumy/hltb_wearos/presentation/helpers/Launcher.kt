package com.croumy.hltb_wearos.presentation.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.croumy.hltb_wearos.presentation.models.Constants
import kotlinx.coroutines.guava.await


class Launcher {
    companion object {
        suspend fun launchRemoteActivity(
            link: String,
            context: Context
        ) {
            // LAUNCH PHONE APP (WHICH WILL SEND BACK THE TOKEN AND USERID)
            val intent = Intent(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.parse(link))

            RemoteActivityHelper(context).startRemoteActivity(intent).await()
        }
    }
}