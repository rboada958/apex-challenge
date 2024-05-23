package com.androidev.my_app_compose.domain.repository

import com.androidev.my_app_compose.core.commons.State
import com.androidev.my_app_compose.core.commons.StateImpl
import com.androidev.my_app_compose.core.network.extensions.DataState
import com.androidev.my_app_compose.domain.datasource.RickAndMortyDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class RickAndMortyRepositoryImpl @Inject constructor(
    private val rickAndMortyDataSource: RickAndMortyDataSource
) : RickAndMortyRepository {
    private var state: MutableStateFlow<StateImpl> = MutableStateFlow(StateImpl.Loading)

    override suspend fun getCharacterList() {
        rickAndMortyDataSource.getCharacterList()
            .onStart {
                state.value = StateImpl.Loading
            }
            .onEach {
                when (it) {
                    DataState.Loading -> {
                        state.value = StateImpl.Loading
                    }

                    is DataState.Error -> {
                        state.value = StateImpl.Error(it.exception.message ?: "Unknown error")
                    }

                    is DataState.OtherError -> {
                        state.value = StateImpl.Error(it.msg)
                    }

                    is DataState.Success -> {
                        state.value = StateImpl.Success(it.data)
                    }
                }
            }
            .onCompletion { completion ->
                if (completion != null) {
                    completion.printStackTrace()
                    state.value = StateImpl.Error(completion.message ?: "Unknown error")
                }
            }.collect()
    }

    override suspend fun getCharacter(id: String) {
        rickAndMortyDataSource.getCharacter(id)
            .onStart {
                state.value = StateImpl.Loading
            }
            .onEach {
                when (it) {
                    DataState.Loading -> {
                        state.value = StateImpl.Loading
                    }

                    is DataState.Error -> {
                        state.value = StateImpl.Error(it.exception.message ?: "Unknown error")
                    }

                    is DataState.OtherError -> {
                        state.value = StateImpl.Error(it.msg)
                    }

                    is DataState.Success -> {
                        state.value = StateImpl.Success(it.data)
                    }
                }
            }
            .onCompletion { completion ->
                if (completion != null) {
                    completion.printStackTrace()
                    state.value = StateImpl.Error(completion.message ?: "Unknown error")
                }
            }.collect()
    }

    override fun rickAndMortyState(): Flow<State> {
        return state.map {
            when (it) {
                StateImpl.Loading -> {
                    State.Loading
                }

                is StateImpl.Error -> {
                    State.Error(it.msg)
                }

                is StateImpl.Success<*> -> {
                    State.Success(it.response)
                }
            }
        }
    }
}