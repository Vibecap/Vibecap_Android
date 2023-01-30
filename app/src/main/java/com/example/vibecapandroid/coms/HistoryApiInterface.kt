package com.example.vibecapandroid.coms

import androidx.annotation.LongDef
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface HistoryApiInterface {
    @GET("app/album/{member_id}")
    fun getHistoryAll(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path ("member_id") member_id:Long
    ): Call<HistoryAllResponse>

    @GET("app/album/vibe/{vibe_id}")
    fun getHistoryOne(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path ("vibe_id") vibe_id:Long
    ): Call<HistoryOneResponse>

    @POST("app/posts")
    fun postHistoryPosting(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Body jsonBody: HistoryPostingBody
    ):Call<HistoryPostingResponse>
}