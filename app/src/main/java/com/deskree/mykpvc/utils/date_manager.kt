package com.deskree.mykpvc.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale


fun getNumWeekday(): Int {
    return LocalDate.now().dayOfWeek.value - 1
}

fun getDateFormat(pattern: String = "yyyy-MM-dd_HH-mm-ss"): SimpleDateFormat{
    return SimpleDateFormat(pattern, Locale.getDefault())
}
