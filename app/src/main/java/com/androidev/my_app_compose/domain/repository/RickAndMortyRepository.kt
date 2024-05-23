package com.androidev.my_app_compose.domain.repository

import com.androidev.my_app_compose.core.commons.State
import kotlinx.coroutines.flow.Flow

interface RickAndMortyRepository {

    suspend fun getCharacterList()

    suspend fun getCharacter(id: String)

    fun rickAndMortyState(): Flow<State>
}