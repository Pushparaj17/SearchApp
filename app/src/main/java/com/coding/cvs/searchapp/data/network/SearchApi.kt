package com.coding.cvs.searchapp.data.network

import com.coding.cvs.searchapp.data.network.dto.CharacterDto
import com.coding.cvs.searchapp.data.network.dto.CharactersResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchApi {
    @GET("api/character/")
    suspend fun searchCharacters(
        @Query("name") name: String,
    ): CharactersResponseDto

    @GET("api/character/{id}")
    suspend fun getCharacter(
        @Path("id") id: Int,
    ): CharacterDto
}

