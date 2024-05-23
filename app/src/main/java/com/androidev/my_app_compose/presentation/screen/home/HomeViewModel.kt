package com.androidev.my_app_compose.presentation.screen.home

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
class HomeViewModel @Inject constructor(
    private var rickAndMortyRepository: RickAndMortyRepository
) : ViewModel() {

    init {
        getCharacterList()
    }

    private fun getCharacterList() {
        viewModelScope.launch {
            rickAndMortyRepository.getCharacterList()
        }
    }

    private fun rickAndMortyUiState(
        repository: RickAndMortyRepository
    ): Flow<UiState> {
        return repository.rickAndMortyState().map {
            return@map when (it) {
                State.Loading -> UiState.Loading

                is State.Error -> UiState.Error(it.msg)

                is State.Success<*> -> UiState.Success(it.response)
            }
        }
    }

    val uiState: StateFlow<UiState> = rickAndMortyUiState(rickAndMortyRepository).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Loading
    )
}