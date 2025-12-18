package com.coding.cvs.searchapp.domain.usecase

import com.coding.cvs.searchapp.domain.Resource
import com.coding.cvs.searchapp.domain.SearchRepository
import com.coding.cvs.searchapp.domain.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchCharactersUseCase @Inject constructor(
    private val repository: SearchRepository,
) {
    /**
     * Business logic for character search.
     *
     * This returns a [Flow] so the UI can react to:
     * - Loading
     * - Success (list of characters)
     * - Error (message)
     */
    operator fun invoke(query: String): Flow<Resource<List<Character>>> = flow {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            emit(Resource.Success(emptyList()))
            return@flow
        }

        emit(Resource.Loading)
        try {
            val results = repository.searchCharactersByName(trimmed)
            emit(Resource.Success(results))
        } catch (t: Throwable) {
            emit(Resource.Error(t.message ?: "Something went wrong"))
        }
    }
}

