package com.deskree.mykpvc.requests

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.deskree.mykpvc.data.my_classes.SubjectItem
import com.deskree.mykpvc.data.my_classes.conducted_classes.TimetableItem
import com.deskree.mykpvc.data.teacher.TeacherItem
import java.io.FileOutputStream
import java.io.IOException

const val TEACHERS = "https://api.college.ks.ua/api/teachers"
// Х - ідентифікатор викладача в системі. Отримати ідентифікатор можна попередньо отримавши інформацію про викладачів в групі
const val TEACHERS_PHOTO = "https://api.college.ks.ua/api/teachers/avatar/75"
const val TEACHERS_MY_GROUP = "https://api.college.ks.ua/api/teachers/my"
const val CLASSES_MY_GROUP = "https://api.college.ks.ua/api/journals"
// X - ідентифікатор журналу. Ідентифікатор журналу можна отримати зі списку журналів групи студента
const val CONDUCTED_DISCIPLINE_CLASSES = "https://api.college.ks.ua/api/journals/88"


fun getTeachers(){
    val request = getRequest(TEACHERS)
    val response = client.newCall(request).execute()
    val responseBody = response.body?.string()
    response.close()

    val listType = object : TypeToken<List<TeacherItem>>() {}.type
    val teachers: List<TeacherItem> = Gson().fromJson(responseBody, listType)
    teachers.forEach{
        println(it)
    }
    println(teachers.size)
}

fun getMyTeachers(){
    val request = getRequest(TEACHERS_MY_GROUP)
    val response = client.newCall(request).execute()
    val responseBody = response.body?.string()
    response.close()

    val listType = object : TypeToken<List<TeacherItem>>() {}.type
    val teachers: List<TeacherItem> = Gson().fromJson(responseBody, listType)
    teachers.forEach{
        println(it)
    }
    println(teachers.size)
}

fun getTeachersPhoto(){
    val request = getRequest(TEACHERS_PHOTO)

    try {
        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            throw IOException("Failed to download image: $response")
        }

        val inputStream = response.body?.byteStream()

        if (inputStream != null) {
            val outputStream = FileOutputStream("Test.jpg")

            val buffer = ByteArray(4 * 1024)
            var bytesRead: Int

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            outputStream.close()
            inputStream.close()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

}

fun getClassesMyGroup(){
    val request = getRequest(CLASSES_MY_GROUP)
    val response = client.newCall(request).execute()
    val responseBody = response.body?.string()
    response.close()

    val listType = object : TypeToken<List<SubjectItem>>() {}.type
    val subjects: List<SubjectItem> = Gson().fromJson(responseBody, listType)
    subjects.forEach{
        println(it)
    }
    println(subjects.size)

}

fun getConductedDisciplineClasses(){
    val request = getRequest(CONDUCTED_DISCIPLINE_CLASSES)
    val response = client.newCall(request).execute()
    val responseBody = response.body?.string()
    response.close()

    val timetable: TimetableItem = Gson().fromJson(responseBody, TimetableItem::class.java)

    timetable.lessons.forEach {
        println(it)
    }

}