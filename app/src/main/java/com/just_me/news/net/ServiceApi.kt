package com.just_me.news.net

import com.google.gson.JsonObject
import com.just_me.news.RecyclerData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ServiceApi {
    @GET("webview/index.php")
    fun getData(@Query("country") country: String): Call<String>
    @GET("http://www.json-generator.com/api/json/get/bOlpsRPGsy?indent=2")
    fun getRecyclerData(): Call<ArrayList<RecyclerData>>
}