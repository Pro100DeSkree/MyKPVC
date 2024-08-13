package com.deskree.mykpvc.data.journal.journal

import com.google.gson.annotations.SerializedName

data class Lesson(
    @SerializedName("id") val id: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updatet_at") val updatedAt: String,
    @SerializedName("group_id") val groupId: Int,
    @SerializedName("teacher_id") val teacherId: Int,
    @SerializedName("subject_id") val subjectId: Int,
    @SerializedName("lesson_number") val lessonNumber: Int,
    @SerializedName("classwork") val classwork: String,
    @SerializedName("homework") val homework: String,
    @SerializedName("hours") val hours: Int,
    @SerializedName("journal_id") val journalId: Int,
    @SerializedName("lesson_date") val lessonDate: String
)
