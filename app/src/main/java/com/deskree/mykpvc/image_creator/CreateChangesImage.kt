package com.deskree.mykpvc.image_creator

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import androidx.compose.ui.graphics.toArgb
import com.deskree.mykpvc.data.changes.ScheChanges
import com.deskree.mykpvc.data.changes.TableChanges
import com.deskree.mykpvc.ui.theme.imgBackground
import com.deskree.mykpvc.ui.theme.imgStroke
import com.deskree.mykpvc.ui.theme.imgText


class CreateChangesImage {
    private lateinit var canvas: Canvas
    private lateinit var bitmap: Bitmap

    // Налаштування параметрів шрифту
    private val fontNormal = Typeface.create("Roboto", Typeface.NORMAL)
    private val fontBold = Typeface.create("Roboto", Typeface.BOLD)
    private val textSizePx = 30f
    private val textColor = imgText.toArgb()

    // Ширина лінії таблиці
    private val lineThicknessTable = 2

    // відступ від краю зображення
    private val padding = 6

    // Розраховані розміри
    private var tableWidth: Int = 0
    private var tableHeight: Int = 0
    private var rowCount: Int = 0
    private var oneRowCount: Int = 0
    private var textHeight: Int = 0
    private var cellHeight: Int = 0
    private var cellWidths = mutableListOf<Int>()
    private lateinit var tableList: List<ScheChanges>

    init {
        Log.d("Mylog", "INIT Exec")
    }


    private fun sizeCalculation(table: TableChanges) {

        // Якщо список практик не порожній, то визначаємо найдовший рядок(default довжина заголовка)
        var maxLenOneRowChanges = calculateTextWidth(table.onDay, fontNormal).toInt()

        if (table.oneRowChanges.isNotEmpty()) {

            table.oneRowChanges.forEach { row ->
                val textLen = calculateTextWidth(row, fontNormal).toInt() + (10 * 2)
                if (maxLenOneRowChanges < textLen) maxLenOneRowChanges = textLen
            }
        }

        // Визначення максимальної довжини тексту у кожному стовпці(з header)
        tableList = mutableListOf(table.header, *table.changes.toTypedArray())
        val maxColWidths = mutableListOf(
            calculateMaxColWidthsSche(tableList) { it.groupNum },
            calculateMaxColWidthsSche(tableList) { it.lesNum },
            calculateMaxColWidthsSche(tableList) { it.lesToSchedule },
            calculateMaxColWidthsSche(tableList) { it.lesToChanges },
            calculateMaxColWidthsSche(tableList) { it.classNum }
        )

        // Визначення ширини та висоти клітинок
        val sumCellWidths = maxColWidths.sum()

        if (maxLenOneRowChanges > sumCellWidths) {
            val plusLenCell = (maxLenOneRowChanges - sumCellWidths) / 5
            maxColWidths.forEach { colWidths ->
                cellWidths.add(colWidths + plusLenCell)
            }
        } else {
            maxColWidths.forEach { colWidths ->
                cellWidths.add(colWidths + 20)
            }
        }

        textHeight = calculateTextHeight().toInt()
        cellHeight = textHeight + 10

        // Визначення кількості рядків
        rowCount = table.changes.size + table.oneRowChanges.size + 1
        oneRowCount = table.oneRowChanges.size + 1

        // Визначення ширини та висоти таблиці
        tableWidth = cellWidths.sum()
        tableHeight = rowCount * cellHeight


        // Визначення розміру зображення з урахуванням падінгу та товщини ліній таблиці
        val imgWidth = tableWidth + padding + (lineThicknessTable * 2)
        val imgHeight = tableHeight + padding + (lineThicknessTable * 2)

        createImage(imgWidth, imgHeight)
    }

    // Визначення максимальної довжини тексту у кожному стовпці(без header)
    private fun calculateMaxColWidthsSche(
        schedule: List<ScheChanges>,
        columnExtractor: (ScheChanges) -> String
    ): Int {
        val maxColWidthsSche = mutableListOf<Int>()
        schedule.forEach { row ->
            val len = calculateTextWidth(columnExtractor(row), fontNormal)
            maxColWidthsSche.add(len.toInt())
        }
        return maxColWidthsSche.max()
    }

    // Розрахунок висоти рядку з урахуванням шрифту та його розміру
    private fun calculateTextHeight(): Float {
        val paint = Paint().apply {
            textSize = textSizePx
            typeface = fontNormal
        }

        val fontMetrics = paint.fontMetrics
        return fontMetrics.descent - fontMetrics.ascent
    }

    // Розрахунок довжини рядку з урахуванням шрифту та його розміру
    private fun calculateTextWidth(text: String, font: Typeface): Float {
        val paint = Paint().apply {
            textSize = textSizePx
            typeface = font
        }
        return paint.measureText(text)
    }

    private fun createImage(width: Int, height: Int) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        canvas = Canvas(bitmap)
        canvas.drawColor(imgBackground.toArgb())

        Log.d("MyLog", "Image Created: W:$width; H:$height")
        Log.d("MyLog", "Drawing...")
    }

    private fun drawTable() {
        val tableStyle = Paint().apply {
            color = imgStroke.toArgb()
            style = Paint.Style.STROKE
            strokeWidth = lineThicknessTable.toFloat()
            isAntiAlias = false
        }

        var lineLeft = padding
        var lineTop = padding
        var lineRight = tableWidth - (padding / 2)
        var lineBottom = 0

        // Малювання контуру таблиці
        canvas.drawRect(
            lineLeft.toFloat(),
            lineTop.toFloat(),
            (tableWidth + (padding / 1.5)).toFloat(),
            (tableHeight + (padding / 1.5)).toFloat(),
            tableStyle
        )

        // Малювання рядків таблиці
        for (row in 0..rowCount) {
            if (row == 0 || row == rowCount) {
                lineTop += cellHeight
                lineBottom += cellHeight
                continue
            }
            canvas.drawLine(
                lineLeft.toFloat(),
                lineTop.toFloat(),
                (lineRight + padding).toFloat(),
                (lineBottom + padding).toFloat(),
                tableStyle
            )
            lineTop += cellHeight
            lineBottom += cellHeight
        }

        lineTop = padding + (oneRowCount * cellHeight)
        lineRight = padding
        lineBottom = tableHeight

        // Малювання стовпців таблиці
        cellWidths.forEachIndexed { index, cellW ->
            if ((cellWidths.size - 1) != index) {
                lineLeft += cellW
                lineRight += cellW
                canvas.drawLine(
                    lineLeft.toFloat(),
                    lineTop.toFloat(),
                    lineRight.toFloat(),
                    (lineBottom + (padding / 1.5)).toFloat(),
                    tableStyle
                )
            }
        }
    }

    private fun drawText(table: TableChanges) {
        val defTextStyle = Paint().apply {
            textSize = textSizePx
            color = textColor
            typeface = fontNormal
//            isAntiAlias = false
        }
        val boldTextStyle = Paint().apply {
            textSize = textSizePx
            color = textColor
            typeface = fontBold
//            isAntiAlias = false
        }

        var textLeft = tableWidth / 2 - calculateTextWidth(table.onDay, fontNormal) / 2 + padding
        var textTop = cellHeight - padding

        // Малювання тайтлу
        canvas.drawText(table.onDay, textLeft, textTop.toFloat(), defTextStyle)

        // Малювання однорядкових змін(практика/з практики)
        table.oneRowChanges.forEach { lineOfText ->

            textLeft = tableWidth / 2 - calculateTextWidth(lineOfText, fontNormal) / 2 + padding
            textTop += cellHeight

            canvas.drawText(lineOfText, textLeft, textTop.toFloat(), defTextStyle)
        }

        // Малювання основної частини таблиці(включно з header-ом)
        var style = boldTextStyle
        var lastLenString: Float
        var standard = padding.toFloat()
        tableList.forEach { scheChanges ->
            textTop += cellHeight

            lastLenString = calculateTextWidth(scheChanges.groupNum, fontNormal) / 2
            textLeft = standard + (cellWidths[0] / 2) - lastLenString
            standard += cellWidths[0]

            canvas.drawText(scheChanges.groupNum, textLeft, textTop.toFloat(), style)


            lastLenString = calculateTextWidth(scheChanges.lesNum, fontNormal) / 2
            textLeft = standard + (cellWidths[1] / 2) - lastLenString
            standard += cellWidths[1]

            canvas.drawText(scheChanges.lesNum, textLeft, textTop.toFloat(), style)


            lastLenString = calculateTextWidth(scheChanges.lesToSchedule, fontNormal) / 2
            textLeft = standard + (cellWidths[2] / 2) - lastLenString
            standard += cellWidths[2]

            canvas.drawText(scheChanges.lesToSchedule, textLeft, textTop.toFloat(), style)


            lastLenString = calculateTextWidth(scheChanges.lesToChanges, fontNormal) / 2
            textLeft = standard + (cellWidths[3] / 2) - lastLenString
            standard += cellWidths[3]

            canvas.drawText(scheChanges.lesToChanges, textLeft, textTop.toFloat(), style)


            lastLenString = calculateTextWidth(scheChanges.classNum, fontNormal) / 2
            textLeft = standard + (cellWidths[4] / 2) - lastLenString
            standard = padding.toFloat()

            canvas.drawText(scheChanges.classNum, textLeft, textTop.toFloat(), style)

            style = defTextStyle
        }

    }

    fun getBitmap(table: TableChanges): Bitmap {
        // TODO: Обнулення змінних для коректної відмальовки таблиці.
        tableWidth = 0
        tableHeight = 0
        rowCount = 0
        oneRowCount = 0
        textHeight = 0
        cellHeight = 0
        cellWidths = mutableListOf()

        sizeCalculation(table)
        drawTable()
        drawText(table)

        return bitmap
    }
}
