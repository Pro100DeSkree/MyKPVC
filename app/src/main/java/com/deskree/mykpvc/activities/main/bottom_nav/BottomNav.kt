package com.deskree.mykpvc.activities.main.bottom_nav


import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNav(
    navController: NavController
) {
    val listItems = listOf(
        BottomItem.Home,
        BottomItem.Journal,
        BottomItem.Settings
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRout = backStackEntry?.destination?.route

        listItems.forEach { item ->
            NavigationBarItem(
                selected = currentRout == item.route,
                onClick = {
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconID),
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title, style = TextStyle(fontSize = 10.sp)) },
            )
        }
    }
}