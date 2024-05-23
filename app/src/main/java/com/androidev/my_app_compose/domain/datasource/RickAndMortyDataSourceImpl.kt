package com.androidev.my_app_compose.domain.datasource

import com.androidev.my_app_compose.core.network.extensions.DataState
import com.androidev.my_app_compose.core.network.extensions.runApiCall
import com.androidev.my_app_compose.data.remote.RickAndMortyApi
import com.androidev.my_app_compose.domain.model.character.CharacterResponse
import com.androidev.my_app_compose.domain.model.character.CharacterResultsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RickAndMortyDataSourceImpl @Inject constructor(
    private val pokeApi: RickAndMortyApi
) : RickAndMortyDataSource {

    override suspend fun getCharacterList(): Flow<DataState<CharacterResponse>> =
        flow {
            runApiCall(
                success = {
                    emit(DataState.Success(data))
                }
            ) {
                pokeApi.getCharacterList()
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getCharacter(id: String): Flow<DataState<CharacterResultsItem>> =
        flow {
            runApiCall(
                success = {
                    emit(DataState.Success(data))
                }
            ) {
                pokeApi.getCharacter(id)
            }
        }.flowOn(Dispatchers.IO)
}