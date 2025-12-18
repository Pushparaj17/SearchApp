package com.coding.cvs.searchapp.data.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharactersResponseDto(
    val results: List<CharacterDto> = emptyList(),
)

