package com.deskree.mykpvc.utils

import android.content.Context
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import java.io.File
import java.io.FileOutputStream


const val fontUrl = "http://91.205.65.228:5000/fonts" //Global
//const val fontUrl = "http://192.168.0.101:5000/fonts" //Local

fun downloadFont(context: Context, fontName: String) {
    val fontsDir = File(context.getExternalFilesDir(null), "fonts")
    val file = File(fontsDir, fontName)

    if (!file.exists()) {
        Log.d("MyLog","Шрифти: Завантаження...")
        Log.d("MyLog","$fontsDir/$fontName")
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("$fontUrl/$fontName")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val fontData = response.body?.bytes()
                    saveFontToFile(fontData, fontsDir, fontName)
                }
            }
        })
    } else{
        Log.d("MyLog","Шрифти: Вже завантажені")
    }
}

private fun saveFontToFile(fontData: ByteArray?, filesDir: File, fileName: String) {

    if (!filesDir.exists()) {
        filesDir.mkdirs()
    }
    val outputFile = File(filesDir, fileName)
    try {
        val outputStream = FileOutputStream(outputFile)
        outputStream.write(fontData ?: byteArrayOf())
        outputStream.close()
        Log.d("MyLog", "Font saved successfully to $fileName")
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("MyLog", "Error saving font to file: ${e.message}")
    }
}