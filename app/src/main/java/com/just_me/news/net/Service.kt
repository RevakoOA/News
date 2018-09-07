package com.just_me.news.net

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Service {
    @GET("webview/index.php")
    fun getData(@Query("country") country: String): Call<String>
}