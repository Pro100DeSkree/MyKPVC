package com.deskree.mykpvc.data.changes


data class TableChanges(
    val onDay: String,
    val header: ScheChanges = ScheChanges(),
    val oneRowChanges: List<String>,
    val changes: List<ScheChanges>
)
