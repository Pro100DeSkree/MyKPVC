package com.deskree.mykpvc.activities.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.deskree.mykpvc.activities.login.LoginActivity
import com.deskree.mykpvc.activities.login.LoginActivity.Companion.IS_LEGACY_TOKEN
import com.deskree.mykpvc.activities.login.LoginActivity.Companion.IS_LOGIN
import com.deskree.mykpvc.activities.main.routes.AppScaffold
import com.deskree.mykpvc.activities.main.routes.settings.IS_DARK_THEME
import com.deskree.mykpvc.activities.main.routes.settings.IS_DYNAMIC_COLORS
import com.deskree.mykpvc.activities.main.routes.settings.LOGGED_IN_ACCOUNT
import com.deskree.mykpvc.activities.main.routes.settings.Settings
import com.deskree.mykpvc.activities.main.routes.settings.MAIN_PREFERENCE_KEY
import com.deskree.mykpvc.requests.profile.myProfile
import com.deskree.mykpvc.ui.theme.MyKPVCTheme

const val ML = "MyLog"

class MainActivity : ComponentActivity() {
    private var accountToken = ""
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Отримання відповіді від activityLogin
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    recreate()
                } else {
                    finish()
                }
            }

        val pref = getSharedPreferences(MAIN_PREFERENCE_KEY, MODE_PRIVATE)
        // Login
        val activeAccountLogin = pref.getString(LOGGED_IN_ACCOUNT, "").toString()
        accountToken = pref.getString(activeAccountLogin, "").toString()

        // Перевірка токена
        if (accountToken.isEmpty()) {
            startActivityLogin(IS_LOGIN)
        } else {
            myProfile(accountToken, {/* Profile */ }) {
                startActivityLogin(IS_LEGACY_TOKEN)
            }
        }


        setContent {
            val isSystemDarkTheme = isSystemInDarkTheme()
            val darkTheme =
                remember { mutableStateOf(pref.getBoolean(IS_DARK_THEME, isSystemDarkTheme)) }
            val dynamicColors =
                remember { mutableStateOf(pref.getBoolean(IS_DYNAMIC_COLORS, true)) }

            val settings = object : Settings {
                override fun logOut() {
                    pref.edit().putString(LOGGED_IN_ACCOUNT, "").apply()
                    recreate()
                }

                override fun changeTheme(isDark: Boolean) {
                    darkTheme.value = isDark
                    pref.edit().putBoolean(IS_DARK_THEME, isDark).apply()
                }

                override fun changeDynamicColors(isDynamic: Boolean) {
                    dynamicColors.value = isDynamic
                    pref.edit().putBoolean(IS_DYNAMIC_COLORS, isDynamic).apply()
                }
            }

            MyKPVCTheme(darkTheme = darkTheme.value, dynamicColor = dynamicColors.value) {
                AppScaffold(settings)
            }
        }
    }

    private fun startActivityLogin(target: Int) {
        val myIntent = Intent(this, LoginActivity::class.java)
            .putExtra("target", target)
        launcher.launch(myIntent)
    }
}
