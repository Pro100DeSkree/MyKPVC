package com.deskree.mykpvc.data.schedule.not_static

data class OneGroupSchedule(
    val groupNum: Int,
    val schedule: Map<Int, Map<String, Int>> // Номер пари | Пара | Авдиторія
)
