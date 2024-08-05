package com.deskree.mykpvc.activities.main.bottom_nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.deskree.mykpvc.activities.main.routes.home.HomeScreen
import com.deskree.mykpvc.activities.main.routes.settings.Settings
import com.deskree.mykpvc.activities.main.routes.settings.SettingsScreen

@Composable
fun NavGraph(
    navHostController: NavHostController,
    settings: Settings,
    ) {
    val defaultScreen = BottomItem.Home.route
    NavHost(navController = navHostController, startDestination = defaultScreen) {
        composable(BottomItem.Home.route){
            HomeScreen()
        }
        composable(BottomItem.Settings.route){
            SettingsScreen(settings)
        }
    }
}