package com.deskree.mykpvc.bottom_nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.deskree.mykpvc.screens.callSchedule.CallScheduleScreen
import com.deskree.mykpvc.screens.home.HomeScreen
import com.deskree.mykpvc.screens.preference.PreferencesScreen
import com.deskree.mykpvc.screens.schedule.ScheduleScreen

@Composable
fun NavGraph(
    navHostController: NavHostController
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
            PreferencesScreen()
        }
    }
}