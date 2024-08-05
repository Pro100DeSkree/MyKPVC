package com.deskree.mykpvc.activities.main.dialogs

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.Dialog
import com.deskree.mykpvc.activities.main.routes.settings.PREF_IS_SHOW_MSG

@Composable
fun StartDialog(
    openInfoDialog: MutableState<Boolean>,
    pref: SharedPreferences,
){

    Dialog(
        onDismissRequest = {
            openInfoDialog.value = false
        }
    ) {
        StartDialogUI(openInfoDialog) { isNotShowMore ->
            val editor = pref.edit()
            editor.putBoolean(PREF_IS_SHOW_MSG, !isNotShowMore)
            editor.apply()
        }
    }
}