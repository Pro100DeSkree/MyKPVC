package com.deskree.mykpvc.requests.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.deskree.mykpvc.requests.client
import com.deskree.mykpvc.requests.getRequest
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

const val URL_PROFILE = "http://api.college.ks.ua/api/users/profile/my"

@OptIn(DelicateCoroutinesApi::class)
fun myProfile(
    token: String,
    myProfile: (String) -> Unit,
    legacyToken: () -> Unit
    ) {

    GlobalScope.launch {
        // Створення запиту
        val request = getRequest(URL_PROFILE, token)
        try {
            // Виконання запиту та отримання відповіді
            val response = client.newCall(request).execute()
            val responseCode = response.code
            val responseBody = response.body?.string().toString()
            response.close()

            Log.d("MyLog", "$responseCode")
            Log.d("MyLog", responseBody)

            if (responseCode == 200) {
                println("MyProfile: $responseBody")
                myProfile.invoke(responseBody)
            } else if (responseCode == 401) {
                legacyToken.invoke()
            }
        } catch (e: Exception) {
            Log.d("MyLog", "myProfile: $e")
        }
    }
}
