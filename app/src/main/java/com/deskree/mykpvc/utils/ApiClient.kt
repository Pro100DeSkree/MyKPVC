package com.deskree.mykpvc.utils

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject


class ApiClient {
    private val urlCookies = "https://api.college.ks.ua/sanctum/csrf-cookie"
    private val urlLogin = "http://api.college.ks.ua/api/login"
    private val urlProfile = "http://api.college.ks.ua/api/users/profile/my"
    private val urlScheChanges = "http://api.college.ks.ua/api/lessons/shedule/replacements:1"
    private val urlCheckRep = "http://api.college.ks.ua/api/lessons/shedule/replacements/checkrep"

    private var client: OkHttpClient? = null

    init {
        client = OkHttpClient()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun myProfile(
        token: String,
        responseCode: (Int) -> Unit,
        setResponse: (JSONObject) -> Unit
    ){

        // Створення запиту
        val request = Request.Builder()
            .url(urlProfile)
            .header("X-Requested-With", "XMLHttpRequest")
            .header("Authorization", "Bearer $token")
            .build()

        GlobalScope.launch {
            try {
                // Виконання запиту та отримання відповіді
                val response = client!!.newCall(request).execute()
                val responseBody = response.body?.string().toString()

                Log.d("MyLog", "MyAccountRespCode: ${response.code}") // TODO: MyAccountResponseCodeLog
                Log.d("MyLog", "MyAccount: $responseBody") // TODO: MyAccountLog

                if(response.code == 200){
                    val jsonObj = JSONObject(responseBody)
                    setResponse(jsonObj)
                }

                responseCode(response.code)

                response.close()
            } catch (e: Exception) {
                Log.d("MyLog", "MyAccountError: $e") //TODO: MyAccountErrorLog
            }
        }
    }

    // Login
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("CoroutineCreationDuringComposition")
    fun loginProcess(
        login: String,
        password: String,
        errorHint: MutableState<Boolean>,
        loggedIn: (String) -> Unit,
        loginError: (String) -> Unit,
        cookieError: (String) -> Unit
    ) {
        GlobalScope.launch {
            val cookies = getCookies(){ cookieErrorMsg ->
                cookieError(cookieErrorMsg)
            }

            // Створення тіла запиту для передачі логіна та пароля
            val formBody = FormBody.Builder()
                .add("login", login)
                .add("password", password)
                .build()

            // Створення запиту
            val request = Request.Builder()
                .url(urlLogin)
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Cookie", cookies.toString())
                .post(formBody)
                .build()

            try {
                // Виконання запиту та отримання відповіді
                val response = client!!.newCall(request).execute()
                val responseBody = response.body?.string().toString()

                Log.d("MyLog", "RespCode: ${response.code}") //TODO: LoginResponseCodeLog
                Log.d("MyLog", "Login: $responseBody") //TODO: LoginLog

                if(response.code == 401){
                    errorHint.value = true
                } else if(response.code == 200){

                    val token = JSONObject(responseBody).getString("token")
                    errorHint.value = false

                    loggedIn(token)
                }

                response.close()
            } catch (e: Exception) {
                Log.d("MyLog", "LoginError: ${e.message}") //TODO: LoginErrorLog
                loginError(e.toString())
            }
        }
    }

    private fun getCookies(
        error: (String) -> Unit
    ): List<String> {
        // Створення запиту
        val request = Request.Builder()
            .url(urlCookies)
            .build()

        try {
            // Виконання запиту
            val response = client!!.newCall(request).execute()

            // Отримання куків з заголовків відповіді
            val cookies = response.headers("Set-Cookie")

            Log.d("MyLog", "Cookie: $cookies") //TODO: CookieLog

            response.close()
            return cookies
        } catch (e: Exception) {
            Log.d("MyLog", "CookieError: $e") //TODO: CookieErrorLog
            error(e.toString())
            return arrayListOf()
        }
    }
}

