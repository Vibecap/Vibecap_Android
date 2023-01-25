package com.example.vibecapandroid.coms

import com.example.vibecapandroid.coms.ApiKey.Companion.API_KEY
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface HomeApiInterface {
    @Multipart
    @POST("app/vibe/capture")
    fun HomeCapture(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Part("member_id") member_id : Long,
        @Part("extra_info") extra_info : String,
        @Part image_file : MultipartBody.Part
    ): Call<CaptureResponse>

    @GET("getVilageFcst?serviceKey=$API_KEY")
    fun getWeather(
        @Query("dataType") dataType: String,
        @Query("numOfRows") numOfRows : Int,
        @Query("pageNo") pageNo : Int,
        @Query("base_date") baseDate : Int,
        @Query("base_time") baseTime: Int,
        @Query("nx") nx: String,
        @Query("ny") ny: String
    ): Call<WEATHER>

    @POST("app/vibe/capture-without-image")
    fun postWithoutImage(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Body JsonBody : withoutimagedata

    ): Call<CaptureResponse>

}

data class withoutimagedata(
    val member_id: Long,
    val extra_info: String

)




//
//data class extra_info(
//
//    val weather : String,
//    val time : String,
//    val feeling : String
//)




