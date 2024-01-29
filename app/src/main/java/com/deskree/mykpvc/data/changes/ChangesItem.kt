package com.deskree.mykpvc.data.changes

import com.google.gson.annotations.SerializedName

data class ChangesItem(
    @SerializedName("TIMESTAMP_X") val timeStamp: String,
    @SerializedName("NAME") val name: String,
    @SerializedName("PREVIEW_TEXT") val previewText: String
)
