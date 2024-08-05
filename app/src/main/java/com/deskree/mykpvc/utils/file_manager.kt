package com.deskree.mykpvc.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream


fun writeImg(context: Context, bitmap: Bitmap, name: String, child: String) {

    // Отримуємо шлях до зовнішньої теки
    val directory = File(context.getExternalFilesDir(null), child)
    // Перевіряємо наявність теки та створюємо, якщо не існує
    if (!directory.exists()) {
        directory.mkdirs()
    }

    // Створити файл у директорії з ім'ям "дата.png"
    val file = File("$directory", "$name.png")

    val stream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    stream.close()

}

fun readImg(context: Context, fileName: String, child: String): Bitmap {

    // Отримуємо шлях до зовнішньої теки
    val filesDir = File(context.getExternalFilesDir(null), child)
    val file = File(filesDir, "$fileName.png")
    if (file.exists()) {
        val inputStream = file.inputStream()
        val byteArray = inputStream.readBytes()

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
    return Bitmap.createBitmap(300, 100, Bitmap.Config.RGB_565)
}