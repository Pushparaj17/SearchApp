package com.coding.cvs.searchapp.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.cvs.searchapp.domain.Resource
import com.coding.cvs.searchapp.domain.model.Character
import com.coding.cvs.searchapp.domain.usecase.GetCharacterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class CharacterDetailUiState(
    val isLoading: Boolean = true,
    val character: Character? = null,
    val errorMessage: String? = null,
)

/**
 * ViewModel for the detail screen.
 *
 * It reads the `id` navigation argument using [SavedStateHandle] and then loads the character.
 */

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getCharacter: GetCharacterUseCase,
) : ViewModel() {

    private val characterId: Int = checkNotNull(savedStateHandle["id"]) {
        "Missing navigation argument: id"
    }

    val uiState: StateFlow<CharacterDetailUiState> =
        getCharacter(characterId)
            .map { result ->
                when (result) {
                    is Resource.Loading -> CharacterDetailUiState(isLoading = true)
                    is Resource.Success -> CharacterDetailUiState(isLoading = false, character = result.data)
                    is Resource.Error -> CharacterDetailUiState(isLoading = false, errorMessage = result.message)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CharacterDetailUiState(),
            )
}

