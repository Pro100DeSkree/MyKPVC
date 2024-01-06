package com.deskree.mykpvc.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream


fun writeImg(context: Context, bitmap: Bitmap, group_num: String, fileName: String) {

    // Отримуємо шлях до зовнішньої теки
    val directory = File(context.getExternalFilesDir(null), "img/$group_num")
    // Перевіряємо наявність теки та створюємо, якщо не існує
    if (!directory.exists()) {
        directory.mkdirs()
    }

    // Створити файл у директорії з ім'ям "дата.png"
    val file = File("$directory", "$fileName.png")

    val stream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    stream.close()

}

fun readImgCollection(context: Context, path: String): ArrayList<Bitmap> {
    // Отримати шлях до зовнішньої директорії файлів додатка
    val filesDir = File(context.getExternalFilesDir(null), "img")
    val scheduleDir = File("$filesDir/$path")
    val fileList = scheduleDir.list()
    val bitmapList = arrayListOf<Bitmap>()


    if (fileList != null) {
        for (fileName in fileList) {
            val file = File(scheduleDir, fileName)
            val inputStream = file.inputStream()
            val byteArray = inputStream.readBytes()
            val imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            bitmapList.add(imageBitmap)
        }
    }
    return bitmapList
}
