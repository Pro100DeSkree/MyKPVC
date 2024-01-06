package com.deskree.mykpvc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.deskree.mykpvc.bottom_nav.BottomNav
import com.deskree.mykpvc.bottom_nav.NavGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Мій ХПФК")
                },
            )
        },
        bottomBar = { BottomNav(navController)}
    ) { padding ->
        Surface(
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
            )
            .background(MaterialTheme.colorScheme.background)
        ) {
            NavGraph(navController)
        }
    }
}
