package com.deskree.mykpvc.activities.main.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.deskree.mykpvc.activities.main.bottom_nav.BottomItem
import com.deskree.mykpvc.activities.main.bottom_nav.BottomNav
import com.deskree.mykpvc.activities.main.bottom_nav.NavGraph
import com.deskree.mykpvc.activities.main.screens.preference.Listener

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    logOutListener: Listener,
    navController: NavHostController
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Мій ХПФК")
                },
            )
        },
        bottomBar = { BottomNav(navController) }
    ) { padding ->
        Surface(
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
            )
            .background(MaterialTheme.colorScheme.background)
        ) {
            NavGraph(navController, logOutListener)
        }
    }
}
