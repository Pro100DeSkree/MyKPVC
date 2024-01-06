package com.deskree.mykpvc.utils

import java.time.LocalDate
import java.util.Calendar


fun getNumWeekday(): Int {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        // Використовуємо API 26 і вище
        LocalDate.now().dayOfWeek.value - 1
    } else {
        // Використовуємо API нижче 26
        val today = Calendar.getInstance()
        today.get(Calendar.DAY_OF_WEEK) - 1
    }
}
