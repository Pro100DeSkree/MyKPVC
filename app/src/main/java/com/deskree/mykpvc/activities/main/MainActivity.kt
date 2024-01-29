package com.deskree.mykpvc.activities.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.compose.rememberNavController
import com.deskree.mykpvc.activities.login.LoginActivity
import com.deskree.mykpvc.activities.main.bottom_nav.BottomItem
import com.deskree.mykpvc.activities.main.screens.MainScreen
import com.deskree.mykpvc.activities.main.screens.preference.LOGGED_IN_ACCOUNT
import com.deskree.mykpvc.activities.main.screens.preference.Listener
import com.deskree.mykpvc.activities.main.screens.preference.MAIN_PREFERENCE_KEY
import com.deskree.mykpvc.requests.profile.myProfile
import com.deskree.mykpvc.ui.theme.MyKPVCTheme

class MainActivity : ComponentActivity() {
    private var accountToken = ""
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
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
            myProfile(accountToken, {}) {
                startActivityLogin(IS_LEGACY_TOKEN)
            }
        }


//
//        // Запуск python
//        if (!Python.isStarted()) {
//            Python.start(AndroidPlatform(this))
//        }


        setContent {
            val navController = rememberNavController()
            val logOutListener = object : Listener {
                override fun logOut() {
                    pref.edit().putString(LOGGED_IN_ACCOUNT, "").apply()
                    navController.navigate(BottomItem.Home.route)
                    startActivityLogin(IS_LOGIN)
                }
            }

            MyKPVCTheme(darkTheme = true) {
                MainScreen(logOutListener, navController)
            }
        }
    }

    private fun startActivityLogin(target: Int) {
        val myIntent = Intent(this, LoginActivity::class.java)
            .putExtra("target", target)
        launcher.launch(myIntent)
    }

    companion object {
        const val IS_LOGIN = 100
        const val IS_LEGACY_TOKEN = 101
    }
}
