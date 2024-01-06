package com.deskree.mykpvc.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import com.chaquo.python.PyException
import com.chaquo.python.Python
import org.json.JSONObject
import java.io.File


fun drawableTableChanges(
    context: Context,
    response: String,
) {
    val py = Python.getInstance()
    val module = py.getModule("create_table_schedule_changes")
    val fontsDir = File(context.getExternalFilesDir(null), "fonts")

    try {
        val bytes = module.callAttr("get_img_table_schedule_ch", response, fontsDir)
            .toJava(ByteArray::class.java)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        val jsonObj = JSONObject(response)
        val date = jsonObj.getJSONObject("head").getString("date")

        writeImg(context, bitmap, "changes", date)
    } catch (e: PyException) {
        Log.d("MyLog", "drawableTableChanges: ${e.message}")
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
    }
}

fun drawableTableSchedule(context: Context, groupNum: String, response: String) {
    val py = Python.getInstance()
    val module = py.getModule("create_table_schedule")
    val fontsDir = File(context.getExternalFilesDir(null), "fonts")

    try {
        var jsonObj = JSONObject(response)
        jsonObj = jsonObj.getJSONObject(groupNum)
        val dayKeys = jsonObj.keys()

        for ((index, dayKey) in dayKeys.withIndex()) {

            val weekdaySchedule = jsonObj.getJSONObject(dayKey)

            val bytes = module.callAttr(
                "get_img_table_schedule",
                weekdaySchedule.toString(),
                groupNum,
                dayKey,
                fontsDir
            )
                .toJava(ByteArray::class.java)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            writeImg(context, bitmap, groupNum, "$index")
        }


    } catch (e: PyException) {
        Log.d("MyLog", "drawableTableSchedule: ${e.message}")
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
    }
}
// {"382":{"понеділок":{"2":{"lesson_and_teacher":"Фіз.вихов. - Федін ЮМ.","classroom":""},"3":{"lesson_and_teacher":"Правознавство - Мадаєва","classroom":""},"4":{"lesson_and_teacher":"Сист.прогр\/Комп.сист - Левицький \/Бабикін","classroom":""}},"вівторок":{"1":{"lesson_and_teacher":"Арх.комп'ютера\/--- - Максимова\/----","classroom":""},"2":{"lesson_and_teacher":"Економіка орг.вир - Живець А.М.","classroom":""},"3":{"lesson_and_teacher":"Проект.мікропроц.сист. - Уткіна","classroom":""},"4":{"lesson_and_teacher":"Системне програмування - Левицький В.М.","classroom":""}},"середа":{"2":{"lesson_and_teacher":"Комп.сист.та мережі - Бабикін","classroom":""},"3":{"lesson_and_teacher":"Периферійні пристрої - Бездворний","classroom":""},"4":{"lesson_and_teacher":"Економіка\/Правозн - Живець \/Мадаєва","classroom":""}},"четвер":{"2":{"lesson_and_teacher":"Іноземна мова за ПС - Аносова, Власенко","classroom":""},"3":{"lesson_and_teacher":"Периферійні пристрої - Бездворний","classroom":""},"4":{"lesson_and_teacher":"Проект.мікропроц.сист. - Уткіна","classroom":""}},"п'ятниця":{"1":{"lesson_and_teacher":"Проект.мікропроц.сист. - Уткіна","classroom":""},"2":{"lesson_and_teacher":"Системне програмування - Левицький В.М.","classroom":""},"3":{"lesson_and_teacher":"Арх.комп'ютера - Максимова","classroom":""},"4":{"lesson_and_teacher":"Комп.сист.та мережі - Бабикін","classroom":""}}}}