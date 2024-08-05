package com.deskree.mykpvc.activities.login

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.deskree.mykpvc.activities.main.routes.settings.IS_DARK_THEME
import com.deskree.mykpvc.activities.main.routes.settings.IS_DYNAMIC_COLORS
import com.deskree.mykpvc.activities.main.routes.settings.LOGGED_IN_ACCOUNT
import com.deskree.mykpvc.activities.main.routes.settings.MAIN_PREFERENCE_KEY
import com.deskree.mykpvc.requests.profile.login
import com.deskree.mykpvc.ui.theme.MyKPVCTheme

class LoginActivity : ComponentActivity() {
    private var target: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        target = intent.getIntExtra("target", 1)
        val pref = getSharedPreferences(MAIN_PREFERENCE_KEY, MODE_PRIVATE)

        setContent {
            val isSystemDarkTheme = isSystemInDarkTheme()
            val darkTheme =
                remember { mutableStateOf(pref.getBoolean(IS_DARK_THEME, isSystemDarkTheme)) }
            val dynamicColors =
                remember { mutableStateOf(pref.getBoolean(IS_DYNAMIC_COLORS, true)) }

            MyKPVCTheme(darkTheme = darkTheme.value, dynamicColor = dynamicColors.value) {

                val isLoading = remember { mutableStateOf(false) }
                val errorHint = remember { mutableStateOf(false) }
                val login = remember { mutableStateOf("") }
                val password = remember { mutableStateOf("") }
                val cookieErrorMsg = remember { mutableStateOf("") }
                val loginErrorMsg = remember { mutableStateOf("") }

                if (target == IS_LEGACY_TOKEN) {
                    Toast.makeText(this, "Пройдіть повторну авторизацію", Toast.LENGTH_LONG).show()
                }

                Surface(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    errorMassages(cookieErrorMsg, loginErrorMsg)

                    if (isLoading.value) {
                        val isToken = pref.getString(login.value, "").toString()
                        val editor = pref.edit()

                        if (target == IS_LOGIN) {
                            if (isToken.isNotEmpty()) {
                                editor.putString(LOGGED_IN_ACCOUNT, login.value)
                                editor.putString(login.value, isToken)
                                editor.apply()

                                intent.putExtra("token", isToken)
                                setResult(RESULT_OK, intent)
                                finish()
                            } else {
                                loginProcess(
                                    login,
                                    password,
                                    errorHint,
                                    isLoading,
                                    loginErrorMsg,
                                    cookieErrorMsg,
                                    editor
                                )
                            }
                        } else if (target == IS_LEGACY_TOKEN) {
                            Toast.makeText(this, "Токен застарів", Toast.LENGTH_LONG).show()
                            loginProcess(
                                login,
                                password,
                                errorHint,
                                isLoading,
                                loginErrorMsg,
                                cookieErrorMsg,
                                editor
                            )
                        }
                    }

                    LoginScreenUI(
                        login,
                        password,
                        errorHint,
                    ) { isLoadingL ->
                        isLoading.value = isLoadingL
                    }
                }
            }
        }
    }

    private fun loginProcess(
        login: MutableState<String>,
        password: MutableState<String>,
        errorHint: MutableState<Boolean>,
        isLoading: MutableState<Boolean>,
        loginErrorMsg: MutableState<String>,
        cookieErrorMsg: MutableState<String>,
        editor: SharedPreferences.Editor,

        ) {
        login(login.value,
            password.value,
            errorHint,
            { token ->
                editor.putString(LOGGED_IN_ACCOUNT, login.value)
                editor.putString(login.value, token)
                editor.apply()

                intent.putExtra("token", token)
                setResult(RESULT_OK, intent)
                finish()
            },
            { loginErrMsg ->
                loginErrorMsg.value = loginErrMsg
            },
            { cookieErrMsg ->
                cookieErrorMsg.value = cookieErrMsg
            }
        )

        isLoading.value = false
    }

    private fun errorMassages(
        cookieErrorMsg: MutableState<String>,
        loginErrorMsg: MutableState<String>,
    ) {
        if (cookieErrorMsg.value.isNotEmpty() or loginErrorMsg.value.isNotEmpty()) {

            Toast.makeText(
                this,
                "Не вдалось встановити зєднання. Перевірте підключення до інтернету.",
                Toast.LENGTH_SHORT
            ).show()

            Toast.makeText(this, loginErrorMsg.value, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val IS_LOGIN = 100
        const val IS_LEGACY_TOKEN = 101
    }
}
