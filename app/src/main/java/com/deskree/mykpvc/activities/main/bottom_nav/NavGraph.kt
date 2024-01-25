package com.deskree.mykpvc.activities.main.bottom_nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.deskree.mykpvc.activities.main.screens.callSchedule.CallScheduleScreen
import com.deskree.mykpvc.activities.main.screens.home.HomeScreen
import com.deskree.mykpvc.activities.main.screens.preference.Listener
import com.deskree.mykpvc.activities.main.screens.preference.PreferencesScreen

@Composable
fun NavGraph(
    navHostController: NavHostController,
    logOutListener: Listener,
    ) {
    val defaultScreen = BottomItem.Home.route
    NavHost(navController = navHostController, startDestination = defaultScreen) {
        composable(BottomItem.Home.route){
            HomeScreen()
        }
//        composable(BottomItem.Schedule.route){
//            ScheduleScreen()
//        }
        composable(BottomItem.CallSchedule.route){
            CallScheduleScreen()
        }
        composable(BottomItem.Settings.route){
            PreferencesScreen(logOutListener)
        }
    }
}