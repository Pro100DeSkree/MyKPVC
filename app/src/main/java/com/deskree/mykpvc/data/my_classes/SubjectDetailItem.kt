package com.deskree.mykpvc.data.my_classes

import com.google.gson.annotations.SerializedName

data class SubjectDetailItem(
    @SerializedName("kod_subj") val kodSubj: Int,
    @SerializedName("subject_name") val subjectName: String,
    @SerializedName("short_title") val shortTitle: String,
    val id: Int,
    val title: String
)
