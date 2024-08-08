package com.deskree.mykpvc.activities.main.routes.journal

import android.content.Context
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.deskree.mykpvc.activities.main.ML
import com.deskree.mykpvc.activities.main.routes.journal.list_items.GradeCard
import com.deskree.mykpvc.activities.main.routes.journal.list_items.GradeLoading
import com.deskree.mykpvc.activities.main.routes.journal.list_items.JournalSelector
import com.deskree.mykpvc.activities.main.routes.journal.list_items.ProfileCard
import com.deskree.mykpvc.activities.main.routes.settings.LOGGED_IN_ACCOUNT
import com.deskree.mykpvc.activities.main.routes.settings.MAIN_PREFERENCE_KEY
import com.deskree.mykpvc.data.TeacherItem
import com.deskree.mykpvc.data.journal.all_journals.Journals
import com.deskree.mykpvc.data.journal.grades.DisciplineGrades
import com.deskree.mykpvc.requests.journal.getAllJournals
import com.deskree.mykpvc.requests.journal.getJournalMarks
import com.deskree.mykpvc.requests.teachers.getTeacherInfo

@Composable
fun JournalMain() {
    val context = LocalContext.current
    val pref = context.getSharedPreferences(MAIN_PREFERENCE_KEY, Context.MODE_PRIVATE)
    val activeAccountLogin = pref.getString(LOGGED_IN_ACCOUNT, "").toString()
    val accountToken = pref.getString(activeAccountLogin, "").toString()

//    testInvokeJournal(accountToken)

    var listOfJournals by remember { mutableStateOf(arrayOf<Journals>()) }
    var selectedJournalText by remember { mutableStateOf("Не обрано") }
    var selectedJournal by remember { mutableStateOf<Journals?>(null) }
    var teacher by remember { mutableStateOf<TeacherItem?>(null) }
    var isUpdateTeacherInfo by remember { mutableStateOf(false) }
    var disciplineGrades by remember { mutableStateOf<DisciplineGrades?>(null) }

    LaunchedEffect(Unit) {
        getAllJournals(
            accountToken = accountToken,
            returnJournals = { list ->
                listOfJournals = list
                selectedJournalText = list.firstOrNull()?.subject?.subjectName ?: "Не обрано"
                selectedJournal = list.firstOrNull()
                isUpdateTeacherInfo = true
            },
            err = {}
        )
    }

    if (isUpdateTeacherInfo) {
        LaunchedEffect(Unit) {
            getTeacherInfo(
                accountToken = accountToken,
                teacherId = selectedJournal!!.teacherId,
                returnTeacherInfo = { teacherInfo ->
                    teacher = teacherInfo
                },
                err = {}
            )
            getJournalMarks(
                accountToken = accountToken,
                journalId = selectedJournal!!.id,
                returnGrades = { grades ->
                    disciplineGrades = grades
                },
                err = {}
            )
            isUpdateTeacherInfo = false
        }

    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            JournalSelector(
                listOfJournals,
                selectedJournal = selectedJournalText,
                onJournalSelected = { journal ->
                    selectedJournalText = journal.subject.subjectName
                    selectedJournal = journal
                    disciplineGrades = null
                    isUpdateTeacherInfo = true
                }
            )
        }

        item {
            ProfileCard(
                selectedJournal,
                teacher
            )
        }

        if (disciplineGrades != null) {
            itemsIndexed(disciplineGrades!!.marks) { index, item ->
                if (item.maxRange != "0") {
                    GradeCard(
                        title = item.title,
                        date = item.date,
                        maxGrade = item.maxRange,
                        mark = item.mark
                    )
                }
            }
        } else {
            item {
                GradeLoading()
            }
        }
    }
}
