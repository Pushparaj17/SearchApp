package com.coding.cvs.searchapp.domain.usecase

import com.coding.cvs.searchapp.domain.Resource
import com.coding.cvs.searchapp.domain.SearchRepository
import com.coding.cvs.searchapp.domain.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCharacterUseCase @Inject constructor(
    private val repository: SearchRepository,
) {
    /**
     * Business logic for loading a single character by id.
     */
    operator fun invoke(id: Int): Flow<Resource<Character>> = flow {
        emit(Resource.Loading)
        try {
            val character = repository.getCharacter(id)
            emit(Resource.Success(character))
        } catch (t: Throwable) {
            emit(Resource.Error(t.message ?: "Something went wrong"))
        }
    }
}

