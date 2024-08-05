package com.deskree.mykpvc.requests.journal

import android.util.Log
import com.deskree.mykpvc.activities.main.ML
import com.deskree.mykpvc.data.journal.grades.DisciplineGrades
import com.deskree.mykpvc.data.journal.journal.Journal
import com.deskree.mykpvc.data.journal.all_journals.Journals
import com.deskree.mykpvc.requests.client
import com.deskree.mykpvc.requests.getRequest
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val JOURNALS = "https://api.college.ks.ua/api/journals"
const val CONDUCTED_DISCIPLINE_CLASSES = "https://api.college.ks.ua/api/journals/%s"
const val GRADES_DISCIPLINE = "https://api.college.ks.ua/api/journals/%s/marks"


@OptIn(DelicateCoroutinesApi::class)
fun getAllJournals(
    accountToken: String,
    returnJournals: (Array<Journals>) -> Unit,
    err: (String) -> Unit,
) {

    GlobalScope.launch {
        val request = getRequest(JOURNALS, accountToken)

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string().toString()
            response.close()

            val journals = Gson().fromJson(responseBody, Array<Journals>::class.java)

            returnJournals.invoke(journals)
        } catch (e: Exception) {
            err.invoke(e.toString())
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun getJournal(
    accountToken: String,
    journalId: Int,
    returnJournal: (Journal) -> Unit,
    err: (String) -> Unit,
) {
    GlobalScope.launch {
        val request = getRequest(CONDUCTED_DISCIPLINE_CLASSES.format(journalId), accountToken)

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string().toString()
            response.close()

            val journal = Gson().fromJson(responseBody, Journal::class.java)

            returnJournal.invoke(journal)
        } catch (e: Exception) {
            err.invoke(e.toString())
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun getJournalMarks(
    accountToken: String,
    journalId: Int,
    returnGrades: (DisciplineGrades) -> Unit,
    err: (String) -> Unit,
) {
    GlobalScope.launch {
        val request = getRequest(GRADES_DISCIPLINE.format(journalId), accountToken)

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string().toString()
            response.close()

            val grades = Gson().fromJson(responseBody, DisciplineGrades::class.java)

            returnGrades.invoke(grades)
        } catch (e: Exception) {
            err.invoke(e.toString())
        }
    }
}

// TODO: testInvokeJournal
fun testInvokeJournal(accountToken: String) {

//    getAllJournals(accountToken, { journals ->
//        journals.forEach { item ->
//            Log.d(ML, item.subject.title)
//            Log.d(ML, item.id.toString())
//            Log.d(ML, "")
//        }
//    }, {})

// 88 113 163 703 746 1176
//    getJournal(accountToken, 88, { journal ->
//
//        Log.d(ML, journal.lessons.size.toString())
//
//        journal.lessons.forEach {
//            Log.d(ML, it.lessonDate)
//            Log.d(ML, it.classwork)
//            Log.d(ML, it.homework)
//            Log.d(ML, it.hours.toString())
//            Log.d(ML, "")
//        }
//    }, {})

    getJournalMarks(accountToken, 1176, { grades ->

        Log.d(ML, grades.subject.subjectName)
        Log.d(ML, "Size: ${grades.marks.size}")
        Log.d(ML, "")

        grades.marks.forEach {
            Log.d(ML, "title: " + it.title)
            Log.d(ML, "date: " + it.date)
            Log.d(ML, "type_str: " + it.typeStr)
            Log.d(ML, "maxRange: " + it.maxRange)
            Log.d(ML, "mark: " + it.mark)
            Log.d(ML, "")
        }
    }, {
        Log.d(ML, it)
    })

}
