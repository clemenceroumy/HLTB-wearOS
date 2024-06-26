package com.croumy.hltb_wearos.tests

import android.content.Context
import android.util.Log
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.croumy.hltb_wearos.helpers.UISetup
import com.croumy.hltb_wearos.mock.data.CULTOFTHELAMB
import com.croumy.hltb_wearos.mock.workers.SaveTimeWorkerFactory
import com.croumy.hltb_wearos.presentation.MainActivity
import com.croumy.hltb_wearos.presentation.constants.TestTags
import com.croumy.hltb_wearos.presentation.data.interfaces.IAppService
import com.croumy.hltb_wearos.presentation.data.interfaces.IHLTBService
import com.croumy.hltb_wearos.presentation.data.interfaces.IPreferenceService
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.croumy.hltb_wearos.presentation.navigation.NavRoutes
import com.croumy.hltb_wearos.presentation.ui.game.GameDetailsScreen
import com.croumy.hltb_wearos.presentation.ui.game.GameViewModel
import com.croumy.hltb_wearos.presentation.workers.interfaces.IWorkerHelper
import dagger.hilt.android.qualifiers.ApplicationContext
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
    val composableScreen = createComposeRule()

    @Inject
    @ApplicationContext
    lateinit var context: Context

    private lateinit var gameViewModel: GameViewModel

    @Inject
    lateinit var workerFactory: SaveTimeWorkerFactory

    @Inject
    lateinit var workerHelper: IWorkerHelper

    @Inject
    lateinit var appService: IAppService

    @Inject
    lateinit var hltbService: IHLTBService

    @Inject
    lateinit var preferenceService: IPreferenceService
    private val savedState = SavedStateHandle(mapOf(NavRoutes.GameDetails.ID to CULTOFTHELAMB.id))

    // UI
    private lateinit var playButton: SemanticsNodeInteraction
    private lateinit var stopButton: SemanticsNodeInteraction
    private lateinit var cancelButton: SemanticsNodeInteraction
    private lateinit var saveButton: SemanticsNodeInteraction

    @Before
    fun setup() {
        hiltRule.inject()
        gameViewModel = GameViewModel(
            appService,
            hltbService,
            preferenceService,
            savedState,
            workerHelper,
            context
        )

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .setWorkerFactory(workerFactory)
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)

        // SET GAME SESSION VIEW
        composableScreen.setContent { UISetup { GameDetailsScreen(gameViewModel) } }
        composableScreen.onNodeWithText(CULTOFTHELAMB.custom_title).assertExists()

        playButton = composableScreen.onNodeWithTag(TestTags.SESSION_PLAY_BTN, useUnmergedTree = true)
        stopButton = composableScreen.onNodeWithTag(TestTags.SESSION_STOP_BTN, useUnmergedTree = true)
        cancelButton = composableScreen.onNodeWithTag(TestTags.SESSION_CANCEL_BTN, useUnmergedTree = true)
        saveButton = composableScreen.onNodeWithTag(TestTags.SESSION_SAVE_BTN, useUnmergedTree = true)
    }

    @Test
    fun startSession() {
        /********
         * START & CANCEL SESSION
         */
        // SESSION NOT STARTED
        assert(gameViewModel.appService.timer.value.state == TimerState.IDLE)
        // PLAY BTN IS DISPLAYED ...
        playButton
            .assertExists()
            .assertHasClickAction()
        // ... AS PLAY ACTION
        playButton.onChild().assertContentDescriptionEquals("play")
        // CLICK ON BTN TO START SESSION
        //playButton.performClick()
        playButton.performClick()
        // SESSION STARTED
        composableScreen.waitForIdle()
        assert(gameViewModel.appService.timer.value.state == TimerState.STARTED)
        Thread.sleep(2000)
        // PLAY BTN IS DISPLAYED AS PAUSE ACTION
        playButton.onChild().assertContentDescriptionEquals("pause")
        // WAIT FOR SESSION TO CONTINUE AND PAUSE IT
        playButton.performClick()
        playButton.onChild().assertContentDescriptionEquals("play")
        // CHECK THAT SESSION HAS BEEN RUNNING AND IS NOW PAUSED
        assert(gameViewModel.appService.timer.value.time.second == 2)
        assert(gameViewModel.appService.timer.value.state == TimerState.PAUSED)
        // STOP SESSION
        Thread.sleep(1000)
        stopButton.assertExists().assertHasClickAction()
        stopButton.performClick()
        // CHECK THAT SESSION HAS BEEN STOPPED
        assert(gameViewModel.appService.timer.value.state == TimerState.STOPPED)
        Thread.sleep(2000)
        // CANCEL SESSION
        cancelButton.assertExists().assertHasClickAction()
        cancelButton.performClick()
        Thread.sleep(1000)
        assert(gameViewModel.appService.timer.value.state == TimerState.IDLE)

        /********
         * START & SAVE SESSION
         */
        // PLAY BTN IS DISPLAYED ...
        playButton
            .assertExists()
            .assertHasClickAction()
        // ... AS PLAY ACTION
        playButton.onChild().assertContentDescriptionEquals("play")
        // CLICK ON BTN TO START SESSION
        playButton.performClick()
        // SESSION STARTED
        composableScreen.waitForIdle()
        assert(gameViewModel.appService.timer.value.state == TimerState.STARTED)
        Thread.sleep(2000)
        // PLAY BTN IS DISPLAYED AS PAUSE ACTION
        playButton.onChild().assertContentDescriptionEquals("pause")
        // WAIT FOR SESSION TO CONTINUE AND PAUSE IT
        playButton.performClick()
        playButton.onChild().assertContentDescriptionEquals("play")
        // CHECK THAT SESSION HAS BEEN RUNNING AND IS NOW PAUSED
        assert(gameViewModel.appService.timer.value.time.second == 2)
        assert(gameViewModel.appService.timer.value.state == TimerState.PAUSED)
        // STOP SESSION
        Thread.sleep(1000)
        stopButton.assertExists().assertHasClickAction()
        stopButton.performClick()
        // CHECK THAT SESSION HAS BEEN STOPPED
        assert(gameViewModel.appService.timer.value.state == TimerState.STOPPED)
        Thread.sleep(2000)
        // SAVE SESSION
        saveButton.assertExists().assertHasClickAction()
        saveButton.performClick() // LAUNCH WORKER AND WAIT FOR THE FLOW TO FINISH
        assert(gameViewModel.appService.timer.value.state == TimerState.SAVED)
        // WAIT FOR LAUNCHEFFECT OF THE VIEW TO TREAT SAVED STATE
        composableScreen.waitForIdle()
        assert(gameViewModel.appService.timer.value.state == TimerState.IDLE)
    }
}