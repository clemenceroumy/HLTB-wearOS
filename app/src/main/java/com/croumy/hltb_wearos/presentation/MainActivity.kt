package com.croumy.hltb_wearos.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.croumy.hltb_wearos.presentation.navigation.NavGraph
import com.croumy.hltb_wearos.presentation.theme.HLTBwearosTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberSwipeDismissableNavController()

            HLTBwearosTheme {
                NavGraph(navController = navController)
            }
        }
    }
}