package com.croumy.hltb_wearos.helpers

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication


// A custom runner to set up the instrumented application class for tests.
// Used in app/build.gradle: `testInstrumentationRunner = "com.croumy.hltb_wearos.helpers.CustomTestRunner"`
class CustomTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}