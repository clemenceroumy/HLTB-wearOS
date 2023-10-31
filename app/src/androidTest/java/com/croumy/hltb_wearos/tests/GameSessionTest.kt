package com.croumy.hltb_wearos.tests

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.croumy.hltb_wearos.helpers.UISetup
import com.croumy.hltb_wearos.mock.data.CULTOFTHELAMB
import com.croumy.hltb_wearos.presentation.MainActivity
import com.croumy.hltb_wearos.presentation.constants.TestTags
import com.croumy.hltb_wearos.presentation.data.interfaces.IAppService
import com.croumy.hltb_wearos.presentation.data.interfaces.IHLTBService
import com.croumy.hltb_wearos.presentation.data.interfaces.IPreferenceService
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.croumy.hltb_wearos.presentation.navigation.NavRoutes
import com.croumy.hltb_wearos.presentation.ui.game.GameDetailsScreen
import com.croumy.hltb_wearos.presentation.ui.game.GameViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class GameSessionTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val scenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule(order = 2)
    val test = createComposeRule()

    lateinit var gameViewModel: GameViewModel
    @Inject lateinit var appService: IAppService
    @Inject lateinit var hltbService: IHLTBService
    @Inject lateinit var preferenceService: IPreferenceService
    val savedState = SavedStateHandle(mapOf(NavRoutes.GameDetails.ID to CULTOFTHELAMB.id))

    @Before
    fun setup() {
        hiltRule.inject();
        gameViewModel = GameViewModel(
            appService,
            hltbService,
            preferenceService,
            savedState,
            MainActivity.context
        )

        // SET GAME SESSION VIEW & CHECK THAT DATA IS CORRECT
        test.setContent { UISetup { GameDetailsScreen(gameViewModel) } }
        test.onNodeWithText(CULTOFTHELAMB.custom_title).assertExists()
    }

    @Test
    fun startSession() {
       assert(gameViewModel.appService.timer.value.state == TimerState.IDLE)
        val playButton = test.onNodeWithTag(TestTags.SESSION_PLAY_BTN).assertExists().assertHasClickAction()
        test.onNodeWithTag(TestTags.SESSION_PLAY_BTN).performClick()
        // SESSION STARTED
        assert(gameViewModel.appService.timer.value.state == TimerState.STARTED)
    }
}