package com.deskree.mykpvc.requests.teachers

import com.deskree.mykpvc.data.TeacherItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun parseTeachers(responseBody: String): List<TeacherItem> {

    val listType = object : TypeToken<List<TeacherItem>>() {}.type
    val teachers: List<TeacherItem> = Gson().fromJson(responseBody, listType)
    return teachers
}
