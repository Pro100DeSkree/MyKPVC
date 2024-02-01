package com.deskree.mykpvc.data

import com.google.gson.annotations.SerializedName

data class JsonChangesItem(
    @SerializedName("TIMESTAMP_X") val timeStamp: String,
    @SerializedName("NAME") val name: String,
    @SerializedName("PREVIEW_TEXT") val previewText: String
)
