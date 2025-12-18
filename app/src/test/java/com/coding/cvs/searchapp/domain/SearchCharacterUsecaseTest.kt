package com.coding.cvs.searchapp.domain

import com.coding.cvs.searchapp.domain.model.Character
import com.coding.cvs.searchapp.domain.usecase.SearchCharactersUseCase
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
class SearchCharactersUseCaseTest {

    private val repository = mockk<SearchRepository>()

    private lateinit var useCase: SearchCharactersUseCase

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
        useCase = SearchCharactersUseCase(repository)
    }

    @Test
    fun `blank query emits Success emptyList and skips repository`() = runTest {

        val emissions = useCase("   ").toList()

        assertEquals(1, emissions.size)

        coVerify(exactly = 0) { repository.searchCharactersByName(any()) }

    }

    @Test
    fun `valid query emits Loading then Success`() = runTest {

        val characters = listOf(fakeCharacter())

        coEvery { repository.searchCharactersByName("Rick") } returns characters

        val emissions = useCase(" Rick ").toList()

        assertEquals(Resource.Loading, emissions[0])

        assertEquals(Resource.Success(characters), emissions[1])

        coVerify { repository.searchCharactersByName("Rick") }

    }

    @Test
    fun `valid query emits Loading then Error when repository throws`() = runTest {

        coEvery { repository.searchCharactersByName("Rick") } throws Exception("API error")

        val emissions = useCase("Rick").toList()

        assertEquals(Resource.Loading, emissions[0])

        assertEquals(Resource.Error("API error"), emissions[1])

    }
}