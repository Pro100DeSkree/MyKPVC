package com.deskree.mykpvc.requests.teachers

import android.util.Log
import com.deskree.mykpvc.activities.main.ML
import com.deskree.mykpvc.data.TeacherItem
import com.deskree.mykpvc.requests.client
import com.deskree.mykpvc.requests.getRequest
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val COLLEGE_TEACHERS = "https://api.college.ks.ua/api/teachers"
const val MY_TEACHERS = "$COLLEGE_TEACHERS/my"
const val TEACHER_INFO = "$COLLEGE_TEACHERS/%s"
const val TEACHER_AVATAR = "$COLLEGE_TEACHERS/%s/avatar"

@OptIn(DelicateCoroutinesApi::class)
fun getAllTeachers(
    accountToken: String,
    err: (String) -> Unit
) {
    GlobalScope.launch {
        val request = getRequest(COLLEGE_TEACHERS, accountToken)

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string().toString()
            response.close()

            val allTeachers = parseTeachers(responseBody)

        } catch (e: Exception) {
            err(e.toString())
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun getMyTeachers(
    accountToken: String,
    err: (String) -> Unit
) {
    GlobalScope.launch {
        val request = getRequest(MY_TEACHERS, accountToken)

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string().toString()
            response.close()

            val myTeachers = parseTeachers(responseBody)

        } catch (e: Exception) {
            err(e.toString())
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun getTeacherInfo(
    accountToken: String,
    teacherId: Int,
    returnTeacherInfo: (TeacherItem) -> Unit,
    err: (String) -> Unit
) {
    GlobalScope.launch {
        val request = getRequest(TEACHER_INFO.format(teacherId), accountToken)

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string().toString()
            response.close()

            val teacherInfo = Gson().fromJson(responseBody, TeacherItem::class.java)

            returnTeacherInfo.invoke(teacherInfo)
        } catch (e: Exception) {
            err(e.toString())
        }
    }
}
