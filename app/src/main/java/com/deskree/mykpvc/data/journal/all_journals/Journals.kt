package com.deskree.mykpvc.data.journal.all_journals

import com.deskree.mykpvc.data.journal.LessonSubject
import com.google.gson.annotations.SerializedName

data class Journals(
    @SerializedName("id") val id: Int,
    @SerializedName("group_id") val groupId: Int,
    @SerializedName("teacher_id") val teacherId: Int,
    @SerializedName("subject_id") val subjectId: Int,
    @SerializedName("parent_id") val parentId: Int,
    @SerializedName("description") val description: String,
    @SerializedName("color") val color: String,
    @SerializedName("group") val group: Group,
    @SerializedName("subject") val subject: LessonSubject
)
