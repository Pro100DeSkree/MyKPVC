package com.deskree.mykpvc.activities.main.bottom_nav

import com.deskree.mykpvc.R


sealed class BottomItem(val title: String, val iconID: Int, val route: String) {
    object Home: BottomItem("Головна", R.drawable.ic_home, "home")
//    object Schedule: BottomItem("Розклад", R.drawable.ic_list, "schedule")
    object CallSchedule: BottomItem("Розклад дзвінків", R.drawable.ic_clock, "call_schedule")
    object Settings: BottomItem("Налаштування", R.drawable.ic_settings, "settings")
}
