package com.coding.cvs.searchapp.domain

import com.coding.cvs.searchapp.domain.usecase.GetCharacterUseCase
import com.coding.cvs.searchapp.domain.model.Character
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetCharacterUseCaseTest {

    private val repository = mockk<SearchRepository>()

    private lateinit var useCase: GetCharacterUseCase

    fun fakeCharacter(): Character =
        Character(
            id = 1,
            name = "Rick Sanchez",
            imageUrl = "image",
            species = "Human",
            status = "Alive",
            originName = "Earth",
            type = null,
            created = "2017"
        )


    @Before
    fun setup() {

        useCase = GetCharacterUseCase(repository)

    }

    @Test
    fun `emits Loading then Success when repository returns character`() = runTest {

        val character = fakeCharacter()

        coEvery { repository.getCharacter(1) } returns character

        val emissions = useCase(1).toList()

        assertEquals(Resource.Loading, emissions[0])

        assertEquals(Resource.Success(character), emissions[1])

        coVerify { repository.getCharacter(1) }

    }

    @Test
    fun `emits Loading then Error when repository throws`() = runTest {

        coEvery { repository.getCharacter(1) } throws RuntimeException("Network error")

        val emissions = useCase(1).toList()

        assertEquals(Resource.Loading, emissions[0])

        assertEquals(Resource.Error("Network error"), emissions[1])

    }
}