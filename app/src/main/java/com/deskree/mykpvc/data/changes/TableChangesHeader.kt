package com.deskree.mykpvc.data.changes

data class TableChangesHeader(
    val groupNum: String = "№.Гр.",
    val lesNum: String = "Пара",
    val lesToSchedule: String = "Заняття за розкладом",
    val lesToChanges: String = "Заняття за зміною",
    val classNum: String = "Авд."
)
