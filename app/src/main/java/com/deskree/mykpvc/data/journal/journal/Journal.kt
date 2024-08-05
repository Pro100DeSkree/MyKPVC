package com.deskree.mykpvc.data.journal.journal

import com.google.gson.annotations.SerializedName

data class Journal(
    @SerializedName("id") val id: Int,
    @SerializedName("group_id") val groupId: Int,
    @SerializedName("teacher_id") val teachetId: Int,
    @SerializedName("subject_id") val subjectId: Int,
    @SerializedName("parent_id") val parent: Int,
    @SerializedName("description") val description: String,
    @SerializedName("color") val color: String,
    @SerializedName("lessons") val lessons: List<Lesson>
)
