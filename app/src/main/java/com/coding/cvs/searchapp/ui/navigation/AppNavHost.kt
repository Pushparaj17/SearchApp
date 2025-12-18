package com.coding.cvs.searchapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.coding.cvs.searchapp.ui.detail.CharacterDetailScreen
import com.coding.cvs.searchapp.ui.search.SearchScreen

object Routes {
    const val SEARCH = "search"
    const val DETAIL = "detail"
}

/**
 * Defines the app's navigation graph.
 */
@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SEARCH,
    ) {
        composable(route = Routes.SEARCH) {
            val vm = hiltViewModel<com.coding.cvs.searchapp.ui.search.SearchViewModel>()
            val uiState = vm.uiState.collectAsStateWithLifecycle()
            SearchScreen(
                uiState = uiState.value,
                onQueryChange = vm::onQueryChange,
                onCharacterClick = { id ->
                    navController.navigate("${Routes.DETAIL}/$id")
                },
            )
        }

        composable(
            route = "${Routes.DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
        ) { entry ->
            // id is read by the Hilt ViewModel via SavedStateHandle.
            if (entry.arguments?.containsKey("id") != true) return@composable
            val vm = hiltViewModel<com.coding.cvs.searchapp.ui.detail.CharacterDetailViewModel>()
            val uiState = vm.uiState.collectAsStateWithLifecycle()
            CharacterDetailScreen(
                uiState = uiState.value,
                onBack = { navController.popBackStack() },
            )
        }
    }
}

