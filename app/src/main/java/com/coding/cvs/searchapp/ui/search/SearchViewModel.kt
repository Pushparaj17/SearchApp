package com.coding.cvs.searchapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.cvs.searchapp.domain.model.Character
import com.coding.cvs.searchapp.domain.usecase.SearchCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val characters: List<Character> = emptyList(),
    val errorMessage: String? = null,
)


/**
 * UI state for the search screen.
 *
 * Compose reads this state and re-draws the screen whenever it changes.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCharacters: SearchCharactersUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        // Update the text immediately so TextField always shows what the user types.
        _uiState.update { it.copy(query = newQuery, errorMessage = null) }

        val trimmed = newQuery.trim()
        if (trimmed.isEmpty()) {
            // User cleared the text
            searchJob?.cancel()
            _uiState.update { it.copy(isLoading = false, characters = emptyList(), errorMessage = null) }
            return
        }

        // Cancel the previous search so we only show results for the latest query.
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchCharacters(newQuery).collectLatest { result ->
                _uiState.update { current ->
                    when (result) {
                        is com.coding.cvs.searchapp.domain.Resource.Loading ->
                            current.copy(isLoading = true, errorMessage = null)

                        is com.coding.cvs.searchapp.domain.Resource.Success ->
                            current.copy(isLoading = false, characters = result.data, errorMessage = null)

                        is com.coding.cvs.searchapp.domain.Resource.Error ->
                            current.copy(isLoading = false, characters = emptyList(), errorMessage = result.message)
                    }
                }
            }
        }
    }
}
