package com.croumy.hltb_wearos.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.navigation.NavGraph
import com.croumy.hltb_wearos.presentation.theme.HLTBwearosTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // CREATING AN INSTANCE OF THE SERVICE WHEN APP STARTS
    @Inject
    lateinit var appService: AppService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            //val navController = rememberSwipeDismissableNavController()
            val navController = rememberNavController()

            HLTBwearosTheme {
                NavGraph(navController = navController)
            }
        }
    }
}