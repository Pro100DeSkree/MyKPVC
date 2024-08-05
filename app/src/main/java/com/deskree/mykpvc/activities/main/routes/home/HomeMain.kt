package com.deskree.mykpvc.activities.main.routes.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(1) {
                ScheduleChanges()
            }
            items(1) {
                MySchedule()
            }
            items(1) {
                CallSchedule()
            }
        }
    }
}
