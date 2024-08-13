package com.deskree.mykpvc.requests.journal

import android.util.Log
import com.deskree.mykpvc.activities.main.ML
import com.deskree.mykpvc.data.journal.grades.DisciplineGrades
import com.deskree.mykpvc.data.journal.journal.Journal
import com.deskree.mykpvc.data.journal.all_journals.Journals
import com.deskree.mykpvc.requests.client
import com.deskree.mykpvc.requests.getRequest
import com.deskree.mykpvc.requests.urls
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@OptIn(DelicateCoroutinesApi::class)
fun getAllJournals(
    accountToken: String,
    returnJournals: (Array<Journals>) -> Unit,
    err: (String) -> Unit,
) {

    GlobalScope.launch {
        val request = getRequest(urls.JOURNALS, accountToken)

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
        val request = getRequest(urls.CONDUCTED_DISCIPLINE_CLASSES.format(journalId), accountToken)

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
        val request = getRequest(urls.GRADES_DISCIPLINE.format(journalId), accountToken)

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
