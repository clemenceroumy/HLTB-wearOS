package com.croumy.hltbwearos.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.croumy.hltbwearos.MainActivity

class Launcher {
    companion object {
        fun openBrowser(link: String) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(link)
            startActivity(MainActivity.context as Context, intent, null)
        }
    }
}