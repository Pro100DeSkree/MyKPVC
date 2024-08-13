package com.deskree.mykpvc.requests.profile

import android.util.Log
import com.deskree.mykpvc.requests.client
import com.deskree.mykpvc.requests.getRequest
import com.deskree.mykpvc.requests.urls.getUrl
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@OptIn(DelicateCoroutinesApi::class)
fun myProfile(
    token: String,
    myProfile: (String) -> Unit,
    legacyToken: () -> Unit
    ) {

    GlobalScope.launch {
        // Створення запиту
        val request = getRequest(getUrl().URL_PROFILE, token)
        try {
            // Виконання запиту та отримання відповіді
            val response = client.newCall(request).execute()
            val responseCode = response.code
            val responseBody = response.body?.string().toString()
            response.close()

//            Log.d("MyLog", "$responseCode")   // TODO: myProfileLog
//            Log.d("MyLog", responseBody)      // TODO: myProfileLog

            if (responseCode == 200) {
                myProfile.invoke(responseBody)
            } else if (responseCode == 401) {
                legacyToken.invoke()
            }
        } catch (e: Exception) {
            Log.d("MyLog", "myProfile: $e") // TODO: myProfileErrorLog
        }
    }
}
