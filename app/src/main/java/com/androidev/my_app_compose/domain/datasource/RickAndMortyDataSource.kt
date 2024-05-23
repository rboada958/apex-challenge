package com.androidev.my_app_compose.domain.datasource

import com.androidev.my_app_compose.core.network.extensions.DataState
import com.androidev.my_app_compose.domain.model.character.CharacterResponse
import com.androidev.my_app_compose.domain.model.character.CharacterResultsItem
import kotlinx.coroutines.flow.Flow

interface RickAndMortyDataSource {

    suspend fun getCharacterList(): Flow<DataState<CharacterResponse>>

    suspend fun getCharacter(id: String) : Flow<DataState<CharacterResultsItem>>
}