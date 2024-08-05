package com.deskree.mykpvc.utils

import java.time.LocalDate
import java.util.Calendar


fun getNumWeekday(): Int {
    return LocalDate.now().dayOfWeek.value - 1
}
