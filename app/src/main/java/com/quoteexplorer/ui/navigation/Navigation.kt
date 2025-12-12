package com.quoteexplorer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quoteexplorer.QuoteExplorerApp
import com.quoteexplorer.ui.screens.details.DetailsScreen
import com.quoteexplorer.ui.screens.details.DetailsViewModel
import com.quoteexplorer.ui.screens.details.DetailsViewModelFactory
import com.quoteexplorer.ui.screens.favorites.FavoritesScreen
import com.quoteexplorer.ui.screens.favorites.FavoritesViewModel
import com.quoteexplorer.ui.screens.favorites.FavoritesViewModelFactory
import com.quoteexplorer.ui.screens.home.HomeScreen
import com.quoteexplorer.ui.screens.home.HomeViewModel
import com.quoteexplorer.ui.screens.home.HomeViewModelFactory

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Details : Screen("details/{quoteId}") {
        fun createRoute(quoteId: Int) = "details/$quoteId"
    }
    object Favorites : Screen("favorites")
}

@Composable
fun QuoteNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val app = context.applicationContext as QuoteExplorerApp
    val repository = app.repository

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(repository)
            )
            HomeScreen(
                viewModel = viewModel,
                onQuoteClick = { quoteId -> // quoteId is Int now
                    navController.navigate(Screen.Details.createRoute(quoteId))
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favorites.route)
                }
            )
        }

        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("quoteId") { type = NavType.IntType })
        ) { backStackEntry ->
            val quoteId = backStackEntry.arguments?.getInt("quoteId") ?: return@composable
            val viewModel: DetailsViewModel = viewModel(
                factory = DetailsViewModelFactory(repository, quoteId)
            )
            DetailsScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Favorites.route) {
            val viewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModelFactory(repository)
            )
            FavoritesScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
