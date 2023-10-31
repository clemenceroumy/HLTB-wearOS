package com.croumy.hltb_wearos.presentation.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.croumy.hltb_wearos.presentation.data.interfaces.IPreferenceService
import com.croumy.hltb_wearos.presentation.models.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesService @Inject constructor(@ApplicationContext val context: Context): IPreferenceService {
    private val preferences: SharedPreferences = context.getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE)

    override var token: String?
        get() = preferences.getString(Constants.PREFERENCES_TOKEN, null)
        set(value) = value?.let { preferences.edit().putString(Constants.PREFERENCES_TOKEN, value).apply() } ?: run {
            preferences.edit().remove(
                Constants.PREFERENCES_TOKEN
            ).apply()
        }

    override var userId: Int?
        get() = preferences.getInt(Constants.PREFERENCES_USER_ID, 0)
        set(value) = value?.let { preferences.edit().putInt(Constants.PREFERENCES_USER_ID, value).apply() } ?: run {
            preferences.edit().remove(Constants.PREFERENCES_USER_ID).apply()
        }
}