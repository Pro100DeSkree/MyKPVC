package com.deskree.mykpvc.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


const val IPADDRESS = "http://91.205.65.228:8080" //Global NEW
const val IPADDRESS_OLD = "http://91.205.65.228:5000" //Global TODO: Видалити непотрібні конструкції після переходу на 8080
//const val IPADDRESS = "http://192.168.0.101:8080" //Local
//const val IPADDRESS_OLD = "http://192.168.0.101:5000" //Local
const val serverIsNotAvailable = "Відсутнє підключення до інтернету або сервер недоступний."

const val url_course_and_groups = "$IPADDRESS/api/courses_and_groups"
const val url_schedule_changes = "$IPADDRESS/api/schedule_changes"
const val url_schedule = "$IPADDRESS/api"

fun getScheduleChangesData(
    context: Context,
    oldUrl: String = "",
    result: (String) -> Unit,
) {
    var url = url_schedule_changes
    if (oldUrl == IPADDRESS_OLD) {
        url = "$IPADDRESS_OLD/api/schedule_changes"
    }
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            result(response)
        },
        { error ->
            if(oldUrl == "") {
                getScheduleChangesData(context, IPADDRESS_OLD){ response -> result(response) }
            }else{
                Log.d("MyLog", error.toString())
                Toast.makeText(context, serverIsNotAvailable, Toast.LENGTH_SHORT).show()
            }
        }
    )
    queue.add(stringRequest)
}

fun getCoursesAndGroupData(
    context: Context,
    oldUrl: String = "",
    result: (String) -> Unit,
) {
    var url = url_course_and_groups
    if (oldUrl == IPADDRESS_OLD) {
        url = "$IPADDRESS_OLD/api/courses_and_groups"
    }

    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            result(response)
        },
        { error ->
            if(oldUrl == "") {
                getCoursesAndGroupData(context, IPADDRESS_OLD){ response -> result(response) }
            }else{
                Log.d("MyLog", error.toString())
                Toast.makeText(context, serverIsNotAvailable, Toast.LENGTH_SHORT).show()
            }
        }
    )
    queue.add(stringRequest)
}

fun getScheduleData(
    context: Context,
    groupNum: String,
    oldUrl: String = "",
    result: (String) -> Unit,
) {
    var url = "$url_schedule/$groupNum"
    if (oldUrl == IPADDRESS_OLD) {
        url = "$IPADDRESS_OLD/api/$groupNum"
    }

    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            result(response)
        },
        { error ->
            if(oldUrl == "") {
                getScheduleData(context, groupNum, IPADDRESS_OLD){ response -> result(response) }
            }else{
                Log.d("MyLog", error.toString())
                Toast.makeText(context, serverIsNotAvailable, Toast.LENGTH_SHORT).show()
            }
        }
    )
    queue.add(stringRequest)
}
