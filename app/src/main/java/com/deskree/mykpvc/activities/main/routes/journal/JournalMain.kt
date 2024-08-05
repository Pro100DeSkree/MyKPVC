package com.deskree.mykpvc.activities.main.routes.journal

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.deskree.mykpvc.activities.main.routes.settings.LOGGED_IN_ACCOUNT
import com.deskree.mykpvc.activities.main.routes.settings.MAIN_PREFERENCE_KEY
import com.deskree.mykpvc.requests.journal.testInvokeJournal

@Composable
fun JournalMain() {
    val context = LocalContext.current
    val pref = context.getSharedPreferences(MAIN_PREFERENCE_KEY, ComponentActivity.MODE_PRIVATE)
    val activeAccountLogin = pref.getString(LOGGED_IN_ACCOUNT, "").toString()
    val accountToken = pref.getString(activeAccountLogin, "").toString()

    testInvokeJournal(accountToken)
}