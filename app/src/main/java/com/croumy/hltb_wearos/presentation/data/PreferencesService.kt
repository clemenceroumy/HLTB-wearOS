package com.croumy.hltb_wearos.presentation.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.croumy.hltb_wearos.presentation.models.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesService @Inject constructor(@ApplicationContext val context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE)

    var token: String?
        get() = preferences.getString(Constants.PREFERENCES_TOKEN, null)
        set(value) = value?.let { preferences.edit().putString(Constants.PREFERENCES_TOKEN, value).apply() } ?: run {
            preferences.edit().remove(
                Constants.PREFERENCES_TOKEN
            ).apply()
        }

    var userId: String?
        get() = preferences.getString(Constants.PREFERENCES_USER_ID, null)
        set(value) = value?.let { preferences.edit().putString(Constants.PREFERENCES_USER_ID, value).apply() } ?: run {
            preferences.edit().remove(Constants.PREFERENCES_USER_ID).apply()
        }
}