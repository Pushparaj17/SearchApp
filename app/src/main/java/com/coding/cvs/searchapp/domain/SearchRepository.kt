package com.coding.cvs.searchapp.domain

import com.coding.cvs.searchapp.domain.model.Character

interface SearchRepository {
    suspend fun searchCharactersByName(name: String): List<Character>
    suspend fun getCharacter(id: Int): Character
}

