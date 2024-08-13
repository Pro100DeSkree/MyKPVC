package com.deskree.mykpvc.requests.urls

import com.deskree.mykpvc.BuildConfig

fun getUrl(): UrlProvider {
    if (BuildConfig.IS_DEBUG)
        return urlsTest
    else
        return urls
}
