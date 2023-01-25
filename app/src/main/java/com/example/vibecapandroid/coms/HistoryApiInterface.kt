package com.example.vibecapandroid.coms

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface HistoryApiInterface {
    @GET("app/album/{member_id}")
    fun getHistoryAll(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path ("member_id") member_id:Long
    ): Call<HistoryAllResponse>
}