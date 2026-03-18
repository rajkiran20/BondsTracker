package app.le.bondstracker.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AddBond : Screen("add_bond")
    data class BondDetail(val bondId: String = "{bondId}") : Screen("bond_detail/{bondId}") {
        fun createRoute(bondId: String) = "bond_detail/$bondId"
    }
}
