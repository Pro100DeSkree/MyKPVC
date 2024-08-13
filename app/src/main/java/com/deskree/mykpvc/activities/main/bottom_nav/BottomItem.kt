package com.deskree.mykpvc.activities.main.bottom_nav

import com.deskree.mykpvc.R


sealed class BottomItem(val title: String, val iconID: Int, val route: String) {
    object Home: BottomItem("Головна", R.drawable.ic_home, "home")
    object Journal: BottomItem("Журнал", R.drawable.ic_list, "journal")
    object Settings: BottomItem("Налаштування", R.drawable.ic_settings, "settings")
}
