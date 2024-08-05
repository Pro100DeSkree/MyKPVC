package com.deskree.mykpvc.activities.main.routes

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.deskree.mykpvc.activities.main.bottom_nav.BottomNav
import com.deskree.mykpvc.activities.main.bottom_nav.NavGraph
import com.deskree.mykpvc.activities.main.routes.settings.Settings
import com.deskree.mykpvc.activities.main.top_app_bar.AppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    settings: Settings
) {
    val navController = rememberNavController()


    Scaffold(
        topBar = {
            AppBar()
        },
        bottomBar = { BottomNav(navController) }
    ) { padding ->
        Surface(
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
            )
        ) {
            NavGraph(navController, settings)
        }
    }
}