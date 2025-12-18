package com.coding.cvs.searchapp.ui.search

import com.coding.cvs.searchapp.domain.Resource
import com.coding.cvs.searchapp.domain.model.Character
import com.coding.cvs.searchapp.domain.usecase.SearchCharactersUseCase
import com.coding.cvs.searchapp.testing.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `onQueryChange searches and updates state`() = runTest {
        val useCase = mockk<SearchCharactersUseCase>()
        val expected = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                imageUrl = "https://example.com/rick.png",
                species = "Human",
                status = "Alive",
                originName = "Earth",
                type = null,
                created = "Nov 4, 2017",
            ),
        )

        every { useCase.invoke("rick") } returns flowOf(
            Resource.Loading,
            Resource.Success(expected),
        )

        val vm = SearchViewModel(useCase)
        vm.onQueryChange("rick")

        // Let the flow finish.
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals("rick", state.query)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(expected, state.characters)
    }

    @Test
    fun `blank query clears results and does not search`() = runTest {
        val useCase = mockk<SearchCharactersUseCase>()
        every { useCase.invoke(any()) } returns flowOf(Resource.Success(emptyList()))
        val vm = SearchViewModel(useCase)

        vm.onQueryChange("   ")
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals("   ", state.query)
        assertFalse(state.isLoading)
        assertTrue(state.characters.isEmpty())
    }
}
