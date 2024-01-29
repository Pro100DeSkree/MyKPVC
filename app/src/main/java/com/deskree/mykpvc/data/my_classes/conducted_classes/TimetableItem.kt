package com.deskree.mykpvc.data.my_classes.conducted_classes

import com.google.gson.annotations.SerializedName
import com.deskree.mykpvc.data.my_classes.SubjectDetailItem

data class TimetableItem(
    val id: Int,
    @SerializedName("group_id") val groupId: Int,
    @SerializedName("teacher_id") val teacherId: Int,
    @SerializedName("subject_id") val subjectId: Int,
    @SerializedName("parent_id") val parentId: Int,
    val description: String?,
    val color: String,
    val lessons: List<LessonItem>,
    val subject: SubjectDetailItem
)
