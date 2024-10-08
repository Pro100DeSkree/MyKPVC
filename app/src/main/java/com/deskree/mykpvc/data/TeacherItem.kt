package com.deskree.mykpvc.data

import com.google.gson.annotations.SerializedName

data class TeacherItem(
    @SerializedName("kod_prep") val kodPrep: Int,
    @SerializedName("FIO_prep") val fioPrep: String,
    @SerializedName("image") val image: String,
    @SerializedName("id") val id: Int,
    @SerializedName("fullname") val fullName: String,
    @SerializedName("shortname") val shortName: String,
    @SerializedName("shortname_rev") val shortNameRev: String
)
