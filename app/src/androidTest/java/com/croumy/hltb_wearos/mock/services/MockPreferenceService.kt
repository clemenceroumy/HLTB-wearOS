package com.croumy.hltb_wearos.mock.services

import com.croumy.hltb_wearos.presentation.data.interfaces.IPreferenceService
import com.croumy.hltbwearos.BuildConfig

class MockPreferenceService: IPreferenceService {
    override val userId = BuildConfig.USER_ID.toInt()
    override val token = BuildConfig.USER_TOKEN
}