package com.deskree.mykpvc.data.journal.grades

import com.google.gson.annotations.SerializedName

data class Grades(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("date") val date: String,
    @SerializedName("type") val type: String,
    @SerializedName("type_str") val typeStr: String,
    @SerializedName("max_grade") val maxRange: String,
    @SerializedName("mark") val mark: String
)
