package com.deskree.mykpvc.requests.profile

import android.util.Log
import androidx.compose.runtime.MutableState
import com.deskree.mykpvc.requests.client
import com.deskree.mykpvc.requests.getRequest
import com.deskree.mykpvc.requests.urls
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.Request
import org.json.JSONObject


@OptIn(DelicateCoroutinesApi::class)
fun login(
    login: String,
    password: String,
    errorHint: MutableState<Boolean>,
    loggedIn: (String) -> Unit,
    loginError: (String) -> Unit,
    cookieError: (String) -> Unit
) {
    // Створення тіла запиту для передачі логіна та пароля
    val formBody = FormBody.Builder()
        .add("login", login)
        .add("password", password)
        .build()

    GlobalScope.launch {
        val cookies = getCookie() { cookieErrorMsg ->
            cookieError(cookieErrorMsg)
        }

        // Створення запиту з використанням куків та тіла форми
        val request = getRequest(urls.URL_LOGIN, formBody, cookies.toString())
        try {
            // Виконання запиту та отримання відповіді
            val response = client.newCall(request).execute()
            val responseCode = response.code
            val responseBody = response.body?.string().toString()
            response.close()

            when (responseCode) {
                200 -> {
                    val token = JSONObject(responseBody).getString("token")
                    errorHint.value = false

                    loggedIn(token)
                }

                401 -> {
                    errorHint.value = true
                }

                else -> {
                    loginError("Невідома помилка при вході. RespCode: $responseCode")
                }
            }
        } catch (e: Exception) {
            loginError(e.toString())

            Log.d("MyLog", "login: $e")
        }
    }
}


fun getCookie(
    error: (String) -> Unit
): List<String>? {
    // Створення запиту
    val request = getRequest(urls.URL_COOKIE)

    try {
        // Виконання запиту та отримання відповіді
        val response = client.newCall(request).execute()
        val responseCode = response.code

        if (responseCode == 204) {
            // Отримання куків з заголовків відповіді
            val cookies = response.headers("Set-Cookie")
            response.close()

            return cookies
        } else {
            error("Не вдалось отримати cookie. ResCode: $responseCode")
        }

    } catch (e: Exception) {
        error(e.toString())

        Log.d("MyLog", "getCookie: $e")
    }
    return null
}
