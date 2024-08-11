package com.deskree.mykpvc.requests

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

var client = OkHttpClient()

fun getRequest(url: String) : Request {
    return Request.Builder()
        .url(url)
        .build()
}

fun getRequest(url: String, accountToken: String) : Request {
    return Request.Builder()
        .url(url)
        .header("X-Requested-With", "XMLHttpRequest")
        .header("Authorization", "Bearer $accountToken")
        .build()
}

fun getRequest(url: String, formBody: FormBody, cookies: String) : Request {
    return Request.Builder()
        .url(url)
        .header("X-Requested-With", "XMLHttpRequest")
        .header("Cookie", cookies)
        .post(formBody)
        .build()
}