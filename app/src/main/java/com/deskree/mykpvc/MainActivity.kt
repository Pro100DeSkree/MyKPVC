package com.deskree.mykpvc

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.deskree.mykpvc.dialogs.ServerIsNotAvailableDialog
import com.deskree.mykpvc.screens.preference.MAIN_PREFERENCE_KEY
import com.deskree.mykpvc.screens.preference.PREF_GROUP_NUM_KEY
import com.deskree.mykpvc.ui.theme.MyKPVCTheme
import com.deskree.mykpvc.utils.downloadFont
import com.deskree.mykpvc.utils.drawableTableChanges
import com.deskree.mykpvc.utils.drawableTableSchedule
import com.deskree.mykpvc.utils.getScheduleChangesData
import com.deskree.mykpvc.utils.getScheduleData
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firstFontName = "Roboto-Regular.ttf"
        val secondFontName = "Roboto-Bold.ttf"
        downloadFont(this, firstFontName)
        downloadFont(this, secondFontName)

        if (!Python.isStarted()) {  // Запуск python
            Python.start(AndroidPlatform(this))
        }

        val pref = getSharedPreferences(MAIN_PREFERENCE_KEY, MODE_PRIVATE)
        val groupNum = pref.getString(PREF_GROUP_NUM_KEY, "-1")!!
        if (groupNum != "-1") {
            getScheduleData(this, groupNum) { response ->
                drawableTableSchedule(this, groupNum, response)
            }
        }

        getScheduleChangesData(this) { response ->
            val jsonObj = JSONObject(response)
            val isChanges = jsonObj.getJSONObject("head").getBoolean("is_changes")
            if(isChanges) {
                drawableTableChanges(this, response)
            }
        }

        setContent {
            val isNotShowMoreMSG = pref.getBoolean(PREF_IS_SHOW_MSG, true)
            MyKPVCTheme(darkTheme = true) {

                val openInfoDialog = remember { mutableStateOf(isNotShowMoreMSG) }
                if(openInfoDialog.value) {
                    MessagesDialog(openInfoDialog, pref)
                }
                MainScreen()
            }
        }
    }
}

@Composable
fun MessagesDialog(
    openInfoDialog: MutableState<Boolean>,
    pref: SharedPreferences,
){

    Dialog(
        onDismissRequest = {
            openInfoDialog.value = false
        }
    ) {
        ServerIsNotAvailableDialog(openInfoDialog) { isNotShowMore ->
            val editor = pref.edit()
            editor.putBoolean(PREF_IS_SHOW_MSG, !isNotShowMore)
            editor.apply()
        }
    }
}

const val PREF_IS_SHOW_MSG = "is_show_msg"


