package com.deskree.mykpvc.requests.changes

import android.util.Log
import com.deskree.mykpvc.activities.main.ML
import com.deskree.mykpvc.data.JsonChangesItem
import com.deskree.mykpvc.data.changes.TableChanges
import com.google.gson.Gson
import com.deskree.mykpvc.requests.client
import com.deskree.mykpvc.requests.getRequest
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val URL_SCHE_CHANGES = "https://api.college.ks.ua/api/lessons/shedule/replacements:%s"
const val URL_CHECK_REP = "https://api.college.ks.ua/api/lessons/shedule/replacements/checkrep"

@OptIn(DelicateCoroutinesApi::class)
fun getChanges(
    accountToken: String,
    countChangesC: Int = 1,
    returnChanges: (timeStamp: String, tableChanges: TableChanges) -> Unit,
    err: (String) -> Unit
) {
    val countChanges = if (countChangesC in 1..5) countChangesC else 1

    GlobalScope.launch {
        val request = getRequest(URL_SCHE_CHANGES.format(countChanges), accountToken)

//        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string().toString()
            response.close()

            val items = Gson().fromJson(responseBody, Array<JsonChangesItem>::class.java)

            items.forEach {
                val oneRowChanges = mutableListOf<String>()
                val changes = parseHtmlTabel(it.previewText) { row ->
                    oneRowChanges.add(row)
                }

                returnChanges.invoke(
                    it.timeStamp, TableChanges(
                        onDay = it.name,
                        oneRowChanges = oneRowChanges,
                        changes = changes
                    )
                )
            }
//        } catch (e: Exception) {
//            err(e.toString())
//        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun checkRep(
    token: String,
    returnChangesRep: (JsonChangesItem) -> Unit,
    err: (String) -> Unit
) {
    GlobalScope.launch {
        val request = getRequest(URL_CHECK_REP, token)

//        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string().toString()
            response.close()

            val item = Gson().fromJson(responseBody, Array<JsonChangesItem>::class.java)

            returnChangesRep.invoke(item[0])
//        } catch (e: Exception) {
//            err(e.toString())
//        }
    }
}