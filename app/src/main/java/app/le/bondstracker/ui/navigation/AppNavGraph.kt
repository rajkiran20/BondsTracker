package app.le.bondstracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import app.le.bondstracker.ui.addbond.AddBondScreen
import app.le.bondstracker.ui.detail.BondDetailScreen
import app.le.bondstracker.ui.home.HomeScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onAddBondClick = { navController.navigate(Screen.AddBond.route) },
                onBondClick = { bondId ->
                    navController.navigate(Screen.BondDetail().createRoute(bondId))
                }
            )
        }

        composable(Screen.AddBond.route) {
            AddBondScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.BondDetail().route,
            arguments = listOf(
                navArgument("bondId") { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink { uriPattern = "app://bonds/detail/{bondId}" }
            )
        ) { backStack ->
            val bondId = backStack.arguments?.getString("bondId") ?: return@composable
            BondDetailScreen(
                bondId = bondId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
