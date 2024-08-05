package com.deskree.mykpvc.requests.changes

import com.deskree.mykpvc.data.changes.ScheChanges
import org.jsoup.Jsoup

val wordsToCheck =
    listOf("гр. ", "пара", "заняття", "розкладом", "заміною", "ауд", "П.І.Б. викладача")


fun parseHtmlTabel(
    html: String,
    addOneRowChanges: (String) -> Unit
): MutableList<ScheChanges> {
    val doc = Jsoup.parse(html)
    val changesList: MutableList<ScheChanges> = mutableListOf()

    // Отримання всіх елементів <tr> у таблиці
    val paragraphs = doc.select("table tbody tr")

    for (paragraph in paragraphs) {
        val rowItems = paragraph.select("td")

        if (rowItems.text().isNotEmpty()) {

            when (rowItems.size) {
                5 -> {
                    // Перевірка співпадаючих слів
                    val containsWords = wordsToCheck.filter { word ->
                        rowItems.text().contains(word, ignoreCase = true)
                    }
                    if (containsWords.isNotEmpty()) {
                        continue
                    }
                    changesList.add(
                        ScheChanges(
                            groupNum = rowItems[0].text(),
                            lesNum = rowItems[1].text(),
                            lesToSchedule = rowItems[2].text(),
                            lesToChanges = rowItems[3].text(),
                            classNum = rowItems[4].text(),
                        )
                    )
                }
                4 -> {
                    addOneRowChanges.invoke(rowItems.text())
                }
            }
        }
    }
    return changesList
}
