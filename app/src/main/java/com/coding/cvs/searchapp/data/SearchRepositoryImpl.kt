package com.coding.cvs.searchapp.data

import com.coding.cvs.searchapp.data.network.SearchApi
import com.coding.cvs.searchapp.data.network.dto.CharacterDto
import com.coding.cvs.searchapp.domain.DateFormatter
import com.coding.cvs.searchapp.domain.SearchRepository
import com.coding.cvs.searchapp.domain.model.Character
import retrofit2.HttpException

class SearchRepositoryImpl(
    private val api: SearchApi,
) : SearchRepository {

    /**
     * Very small in-memory cache.
     *
     * Why: search results already contain all fields we need for the detail screen,
     * so if the user taps a character we can often avoid a second network call.
     */
    private val inMemoryCacheById: MutableMap<Int, Character> = mutableMapOf()

    override suspend fun searchCharactersByName(name: String): List<Character> {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return emptyList()

        return try {
            val response = api.searchCharacters(name = trimmed)
            val mapped = response.results.map { it.toDomain() }
            mapped.forEach { inMemoryCacheById[it.id] = it }
            mapped
        } catch (e: HttpException) {
            // The API returns HTTP 404 when there are no matches.
            if (e.code() == 404) emptyList() else throw e
        }
    }

    override suspend fun getCharacter(id: Int): Character {
        inMemoryCacheById[id]?.let { return it }

        // Fetch from the API if we don't already have it.
        val mapped = api.getCharacter(id = id).toDomain()
        inMemoryCacheById[id] = mapped
        return mapped
    }
}

private fun CharacterDto.toDomain(): Character {
    val cleanType = type.trim().ifEmpty { null }
    return Character(
        id = id,
        name = name,
        imageUrl = image,
        species = species,
        status = status,
        originName = origin.name,
        type = cleanType,
        created = DateFormatter.formatCreatedDate(created),
    )
}

