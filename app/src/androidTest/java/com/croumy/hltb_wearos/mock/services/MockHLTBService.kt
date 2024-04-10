package com.croumy.hltb_wearos.mock.services

import SearchRequest
import SubmitRequest
import android.util.Log
import com.croumy.hltb_wearos.mock.data.CULTOFTHELAMB
import com.croumy.hltb_wearos.presentation.data.interfaces.IHLTBService
import com.croumy.hltb_wearos.presentation.models.api.AddGameRequest
import com.croumy.hltb_wearos.presentation.models.api.Game
import com.croumy.hltb_wearos.presentation.models.api.GameRequest
import com.croumy.hltb_wearos.presentation.models.api.SearchResponse
import com.croumy.hltb_wearos.presentation.models.api.UserResponse
import kotlinx.coroutines.delay

class MockHLTBService: IHLTBService {
    override suspend fun getUser(): UserResponse? {
        Log.i("MockHLTBService", "getUser()")
        return null
    }

    override suspend fun getGames(gameRequest: GameRequest): List<Game> {
        Log.i("MockHLTBService", "getGames()")
        return listOf(CULTOFTHELAMB)
    }

    override suspend fun submitTime(request: SubmitRequest) {
        Log.i("MockHLTBService", "submitTime()")
        delay(2000)
    }

    override suspend fun searchGame(request: SearchRequest): SearchResponse? {
        Log.i("MockHLTBService", "searchGame()")
        return null
    }

    override suspend fun addGame(addGameRequest: AddGameRequest): Boolean  {
        Log.i("MockHLTBService", "addGame()")
        return false
    }
}