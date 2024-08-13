package com.deskree.mykpvc.requests.teachers

import com.deskree.mykpvc.data.TeacherItem
import com.deskree.mykpvc.requests.client
import com.deskree.mykpvc.requests.getRequest
import com.deskree.mykpvc.requests.urls.getUrl
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
fun getAllTeachers(
    accountToken: String,
    err: (String) -> Unit
) {
    GlobalScope.launch {
        val request = getRequest(getUrl().COLLEGE_TEACHERS, accountToken)

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
        val request = getRequest(getUrl().MY_TEACHERS, accountToken)

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
        val request = getRequest(getUrl().TEACHER_INFO.format(teacherId), accountToken)

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
