package com.androidev.my_app_compose.presentation.screen.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidev.my_app_compose.core.commons.State
import com.androidev.my_app_compose.core.commons.UiState
import com.androidev.my_app_compose.domain.repository.RickAndMortyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private var rickAndMortyRepository: RickAndMortyRepository
) : ViewModel() {

    fun getCharacter(id: String) {
        viewModelScope.launch {
            rickAndMortyRepository.getCharacter(id)
        }
    }

    private fun rickAndMortyUiState(
        repository: RickAndMortyRepository
    ): Flow<UiState> {
        return repository.rickAndMortyState()
            .map {
                print(it)
                return@map when (it) {
                    State.Loading -> UiState.Loading

                    is State.Error -> UiState.Error(it.msg)

                    is State.Success<*> -> UiState.Success(it.response)
                }
            }
    }

    val uiState: StateFlow<UiState> =
        rickAndMortyUiState(rickAndMortyRepository)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = UiState.Loading
            )
}