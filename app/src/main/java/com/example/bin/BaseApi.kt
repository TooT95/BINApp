package com.example.bin

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BaseApi {

    @GET("/{bin}")
    fun bin(@Path("bin") bin: String): Call<ResponseBody>
}