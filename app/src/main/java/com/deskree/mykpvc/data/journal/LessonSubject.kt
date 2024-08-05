package com.deskree.mykpvc.data.journal

import com.google.gson.annotations.SerializedName

data class LessonSubject(
    @SerializedName("kod_subj") val kodSubj: Int,
    @SerializedName("subject_name") val subjectName: String,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("short_title") val shortTitle: String
)
