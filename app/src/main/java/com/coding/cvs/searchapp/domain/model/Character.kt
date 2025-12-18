package com.coding.cvs.searchapp.domain.model

data class Character(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val species: String,
    val status: String,
    val originName: String,
    val type: String?,
    val created: String,
)

