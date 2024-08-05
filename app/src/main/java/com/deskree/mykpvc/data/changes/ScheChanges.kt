package com.deskree.mykpvc.data.changes

data class ScheChanges(
    val groupNum: String = "№.Гр.",
    val lesNum: String = "Пара",
    val lesToSchedule: String = "Заняття за розкладом",
    val lesToChanges: String = "Заняття за зміною",
    val classNum: String = "Авд."
)
