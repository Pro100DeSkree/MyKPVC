package com.deskree.mykpvc.requests.changes

import com.deskree.mykpvc.data.JsonChangesItem
import com.deskree.mykpvc.data.changes.TableChanges
import com.google.gson.Gson
import com.deskree.mykpvc.requests.client
import com.deskree.mykpvc.requests.getRequest
import com.deskree.mykpvc.requests.urls
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@OptIn(DelicateCoroutinesApi::class)
fun getChanges(
    token: String,
    countChangesC: Int = 1,
    returnChanges: (List<TableChanges>) -> Unit,
    err: (String) -> Unit
){
    val countChanges = if (countChangesC in 1..5) countChangesC else 1

    GlobalScope.launch {
        val changesList: MutableList<TableChanges> = mutableListOf()
        val request = getRequest(urls.URL_SCHE_CHANGES.format(countChanges), token)

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string().toString()
            response.close()

            val items = Gson().fromJson(responseBody, Array<JsonChangesItem>::class.java)

            items.forEach {
                val oneRowChanges = mutableListOf<String>()
                val changes = parseHtmlTabel(it.previewText) { row ->
                    oneRowChanges.add(row)
                }

                changesList.add(
                    TableChanges(
                        onDay = it.name,
                        oneRowChanges = oneRowChanges,
                        changes = changes
                    )
                )
            }
            returnChanges.invoke(changesList)
        } catch (e: Exception){
            err(e.toString())
        }
    }
}