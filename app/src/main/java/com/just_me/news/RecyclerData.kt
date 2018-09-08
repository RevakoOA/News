package com.just_me.news

import com.google.gson.annotations.SerializedName

data class RecyclerData(
        @SerializedName("about") val about: String = "",
        @SerializedName("_id") val id: String = "",
        @SerializedName("image") val image: String = "",
        @SerializedName("title") val title: String = ""
)