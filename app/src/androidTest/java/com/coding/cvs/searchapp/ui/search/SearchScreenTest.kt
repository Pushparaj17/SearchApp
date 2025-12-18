package com.coding.cvs.searchapp.ui.search

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.coding.cvs.searchapp.domain.model.Character
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun typingInSearchFieldCallsOnQueryChange() {
        var latestQuery = ""

        composeRule.setContent {
            SearchScreen(
                uiState = SearchUiState(),
                onQueryChange = { latestQuery = it },
                onCharacterClick = {},
            )
        }

        composeRule.onNodeWithTag("searchField").performTextInput("push")
        assertEquals("push", latestQuery)
    }

    @Test
    fun gridShowsCharactersAndClickCallsCallback() {
        var clickedId: Int? = null
        val character = Character(
            id = 1,
            name = "Pushparaj Ponnaiah",
            imageUrl = "https://example.com/push.png",
            species = "Human",
            status = "Alive",
            originName = "Earth",
            type = null,
            created = "Nov 4, 2017",
        )

        composeRule.setContent {
            SearchScreen(
                uiState = SearchUiState(
                    query = "push",
                    isLoading = false,
                    characters = listOf(character),
                ),
                onQueryChange = {},
                onCharacterClick = { clickedId = it },
            )
        }

        composeRule.onNodeWithText("Pushparaj Ponnaiah").assertIsDisplayed()
        composeRule.onNodeWithTag("characterItem_1").performClick()
        assertEquals(1, clickedId)
    }
}
