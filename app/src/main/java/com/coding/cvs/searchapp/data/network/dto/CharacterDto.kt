package com.coding.cvs.searchapp.data.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: OriginDto,
    val location: LocationDto,
    val image: String,
    val created: String,
)

@JsonClass(generateAdapter = true)
data class OriginDto(
    val name: String,
    val url: String,
)

@JsonClass(generateAdapter = true)
data class LocationDto(
    val name: String,
    val url: String,
)

