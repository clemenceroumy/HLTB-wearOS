package com.croumy.hltb_wearos.presentation.data.interfaces

import SearchRequest
import SubmitRequest
import com.croumy.hltb_wearos.presentation.models.api.AddGameRequest
import com.croumy.hltb_wearos.presentation.models.api.Game
import com.croumy.hltb_wearos.presentation.models.api.GameRequest
import com.croumy.hltb_wearos.presentation.models.api.SearchResponse
import com.croumy.hltb_wearos.presentation.models.api.UserResponse

interface IHLTBService {
    suspend fun getUser(): UserResponse?
    suspend fun getGames(gameRequest: GameRequest = GameRequest()): List<Game>
    suspend fun submitTime(request: SubmitRequest)
    suspend fun searchGame(request: SearchRequest): SearchResponse?
    suspend fun addGame(addGameRequest: AddGameRequest): Boolean
}