package com.deskree.mykpvc.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.deskree.mykpvc.activities.main.ML
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

const val SCHE_CHANGES_DIR_NAME = "sche_changes"

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

fun readAllImgs(context: Context, child: String, theme: String, returnBitmap: (Bitmap) -> Unit) {
    // Отримуємо шлях до зовнішньої теки
    val filesDir = File(context.getExternalFilesDir(null), child)

    val sortedFiles = getSortedFilesByDate(filesDir, theme)
    sortedFiles.forEachIndexed() { index, file ->
        if (index <= 4)
            returnBitmap.invoke(readImg(context, file.name, child))
        else
            file.delete()
    }
}

fun readImg(context: Context, fileName: String, child: String): Bitmap {

    // Отримуємо шлях до зовнішньої теки
    val filesDir = File(context.getExternalFilesDir(null), child)
    val file = File(filesDir, fileName)
    if (file.exists()) {
        val inputStream = file.inputStream()
        val byteArray = inputStream.readBytes()

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
    return Bitmap.createBitmap(300, 100, Bitmap.Config.RGB_565)
}

fun getSortedFilesByDate(filesDir: File, theme: String): List<File> {
    // Створюємо форматер для дати
    val dateFormat = getDateFormat()

    // Зчитуємо файли з директорії та фільтруємо лише файли з "-night.png" у кінці
    val files = filesDir.listFiles { _, name -> name.endsWith("-$theme.png") } ?: return emptyList()

    // Сортуємо файли за датою, яку ми витягуємо з назви файлу
    return files.sortedByDescending { file ->
        val fileName = file.name.removeSuffix("-$theme.png")
        dateFormat.parse(fileName)
    }
}

fun isNoImage(context: Context, fileName: String?, child: String, theme: String): Boolean {
    val filesDir = File(context.getExternalFilesDir(null), child)

    // Якщо цей каталог існує і якщо це каталог
    if (filesDir.exists() && filesDir.isDirectory) {

        if (fileName.isNullOrEmpty()) {
            // Перевіряємо пустий каталог чи ні
            val files = filesDir.listFiles() { _, name -> name.endsWith("-$theme.png") }
            return files.isNullOrEmpty()
        } else {
            // Перевіряємо чи існує зображення з імям name
            val files = filesDir.listFiles()

            if (files != null) {
                files.forEach { file ->

                    if (file.name == "$fileName.png")
                        return false
                }
            }
            return true
        }
    } else
        return true
}