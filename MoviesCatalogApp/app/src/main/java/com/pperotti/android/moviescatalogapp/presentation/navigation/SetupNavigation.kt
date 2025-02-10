package com.pperotti.android.moviescatalogapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pperotti.android.moviescatalogapp.presentation.details.DetailsScreen
import com.pperotti.android.moviescatalogapp.presentation.main.MainScreen

@Composable
fun SetupNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home_screen"
    ) {
        composable(route = "home_screen") {
            MainScreen(
                onMovieSelected = { id ->
                    navController.navigate("details_screen/?id=$id")
                }
            )
        }
        composable(
            route = "details_screen/?id={id}",
            arguments = listOf(navArgument("id") { defaultValue = -1 })
        ) {
            val id = it.arguments?.getInt("id")
            requireNotNull(id)
            DetailsScreen(
                id = id,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}