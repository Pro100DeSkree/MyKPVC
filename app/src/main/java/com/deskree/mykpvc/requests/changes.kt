package com.deskree.mykpvc.requests

import com.deskree.mykpvc.data.JsonChangesItem
import com.deskree.mykpvc.data.changes.TableChanges
import com.google.gson.Gson
import com.deskree.mykpvc.parser.parseHtml
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val URL_SCHE_CHANGES = "https://api.college.ks.ua/api/lessons/shedule/replacements:"

@OptIn(DelicateCoroutinesApi::class)
fun getChanges(
    token: String,
    countChangesC: Int = 1,
    returnChanges: (List<TableChanges>) -> Unit
){
    val countChanges = if (countChangesC in 1..5) countChangesC else 1

    GlobalScope.launch {
        val changesList: MutableList<TableChanges> = mutableListOf()
        val request = getRequest("$URL_SCHE_CHANGES$countChanges", token)

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string().toString()
            response.close()

            val items = Gson().fromJson(responseBody, Array<JsonChangesItem>::class.java)

            items.forEach {
                val oneRowChanges = mutableListOf<String>()
                val changes = parseHtml(it.previewText) { row ->
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
        } catch (_: Exception){ }
    }
}