package com.example.vibecapandroid.coms

import android.net.Uri
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.Objects


interface VibeApiInterface {
    @Multipart
    @POST("app/vibe/capture")
    fun VibeCapture(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Header("Content-Type") multipart: String,
        @Part("member_id") member_id : Long,
        @Part("extra_info") extra_info : extra_info,
        @Part image_file : MultipartBody.Part
    ): Call<CaptureResponse>

    @Multipart
    @POST("/app/vibe/capture-from-album")
    fun VibeCapture_from_album(
    ):Call<CaptureResponse>

}