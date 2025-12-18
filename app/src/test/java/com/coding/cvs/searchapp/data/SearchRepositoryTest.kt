package com.coding.cvs.searchapp.data

import com.coding.cvs.searchapp.data.network.SearchApi
import com.coding.cvs.searchapp.data.network.dto.CharacterDto
import com.coding.cvs.searchapp.data.network.dto.CharactersResponseDto
import com.coding.cvs.searchapp.data.network.dto.LocationDto
import com.coding.cvs.searchapp.data.network.dto.OriginDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryTest {

    private val api = mockk<SearchApi>()
    private lateinit var repository: SearchRepositoryImpl

    @Before
    fun setup() {
        repository = SearchRepositoryImpl(api)
    }



    @Test
    fun `returns empty list when name is blank and does not call api`() = runTest {
        val result = repository.searchCharactersByName("   ")

        assertTrue(result.isEmpty())
        coVerify(exactly = 0) { api.searchCharacters(any()) }
    }

    @Test
    fun `successful search maps dto to domain and caches result`() = runTest {
        coEvery { api.searchCharacters("Rick") } returns fakeResponse()

        val result = repository.searchCharactersByName("Rick")

        assertEquals(1, result.size)
        assertEquals(1, result.first().id)
        assertEquals("Rick Sanchez", result.first().name)

        coVerify { api.searchCharacters("Rick") }
    }

    @Test
    fun `404 error returns empty list`() = runTest {
        coEvery { api.searchCharacters("push") } throws http404()

        val result = repository.searchCharactersByName("push")

        assertTrue(result.isEmpty())
    }

    @Test(expected = HttpException::class)
    fun `non-404 error is rethrown`() = runTest {
        coEvery { api.searchCharacters("boom") } throws http500()

        repository.searchCharactersByName("boom")
    }



    @Test
    fun `getCharacter returns cached value when available`() = runTest {
        // First search populates cache
        coEvery { api.searchCharacters("Rick") } returns fakeResponse()

        repository.searchCharactersByName("Rick")

        val character = repository.getCharacter(1)

        assertEquals("Rick Sanchez", character.name)
        coVerify(exactly = 0) { api.getCharacter(any()) }
    }

    @Test
    fun `getCharacter fetches from api when not cached`() = runTest {
        coEvery { api.getCharacter(1) } returns fakeCharacterDto()

        val character = repository.getCharacter(1)

        assertEquals(1, character.id)
        assertEquals("Rick Sanchez", character.name)

        coVerify { api.getCharacter(1) }
    }


    private fun fakeResponse(): CharactersResponseDto =
        CharactersResponseDto(results = listOf(fakeCharacterDto()))

    private fun fakeCharacterDto(): CharacterDto =
        CharacterDto(
            id = 1,
            name = "Rick Sanchez",
            image = "image",
            species = "Human",
            status = "Alive",
            type = "",
            created = "2017-11-04T18:48:46.250Z",
            origin = OriginDto(name = "Earth", url = ""),
            location = LocationDto(name = "Earth", url = ""),
            gender = "Male",
        )

    private fun http404(): HttpException =
        HttpException(
            Response.error<Any>(
                404,
                "{}".toResponseBody("application/json".toMediaType())
            )
        )

    private fun http500(): HttpException =
        HttpException(
            Response.error<Any>(
                500,
                "{}".toResponseBody("application/json".toMediaType())
            )
        )
}