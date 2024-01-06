package com.deskree.mykpvc.screens.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.deskree.mykpvc.dialogs.ChooseCourseDialog
import com.deskree.mykpvc.dialogs.ChooseGroupDialog
import com.deskree.mykpvc.utils.drawableTableSchedule
import com.deskree.mykpvc.utils.getCoursesAndGroupData
import com.deskree.mykpvc.utils.getScheduleData
import org.json.JSONObject

@Composable
fun ChooseGroup(
    pref: SharedPreferences,
    endChange: (String) -> Unit
) {
    val courses = remember { mutableListOf<String>() }
    val chooseCourse = remember { mutableStateOf("") }
    val groups = remember { mutableMapOf<String, ArrayList<String>>() }
    val chooseGroup = remember { mutableStateOf("") }
    val openChooseCourseDialog = remember { mutableStateOf(false) }
    val openChooseGroupDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (groups.isEmpty()) {
        getCoursesAndGroupData(LocalContext.current) { response ->

            val obj = JSONObject(response)
            val keysIterator = obj.keys()

            // Отримуємо всі ключі(Курси)
            val localCourses = arrayListOf<String>()
            for (key in keysIterator) {
                localCourses.add(key)
            }
            courses.addAll(localCourses)

            // Отримуємо всі значення(групи)
            for (key in localCourses) {
                val localGroups = arrayListOf<String>()
                val values = obj.getJSONObject(key).keys()
                for (value in values) {
                    localGroups.add(value)
                }

                groups[key] = localGroups
            }
            openChooseCourseDialog.value = true
        }
    }
    if (openChooseCourseDialog.value) {
        Dialog(
            onDismissRequest = {
                openChooseCourseDialog.value = false
            }
        ) {
            ChooseCourseDialog(courses, openChooseCourseDialog) { course ->
                chooseCourse.value = course
                openChooseGroupDialog.value = true
            }
        }
    }
    if (openChooseGroupDialog.value && chooseCourse.value.isNotEmpty()) {
        Dialog(
            onDismissRequest = {
                openChooseGroupDialog.value = false
            }
        ) {
            ChooseGroupDialog(
                groups[chooseCourse.value]!!,
                openChooseGroupDialog,
                onClickBack = {
                    openChooseCourseDialog.value = true
                },
                onClickGroup = { group ->
                    chooseGroup.value = group
                }
            )
        }
    }
    if (chooseGroup.value.isNotEmpty()){
        val editor = pref.edit()
        editor.putString(PREF_GROUP_NUM_KEY, chooseGroup.value)
        editor.apply()
        getScheduleData(context, chooseGroup.value) { response ->
            drawableTableSchedule(context, chooseGroup.value, response)
        }
        endChange.invoke(chooseGroup.value)
    }
}

interface MyListener{
    fun isSelectedGroup(context: Context, pref: SharedPreferences)
}