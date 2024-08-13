package com.deskree.mykpvc.data.journal.all_journals

import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("st_count") val stCount: Int,
    @SerializedName("year_start") val yearStart: String,
    @SerializedName("teacher_id") val teacherId: Int,
    @SerializedName("specialty_id") val specialityId: Int
)
