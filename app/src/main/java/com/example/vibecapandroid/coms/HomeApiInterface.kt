package com.example.vibecapandroid.coms
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface HomeApiInterface {
    @Multipart
    @POST("app/vibe/capture")
    fun postCapture(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Part("member_id") member_id : Long,
        @Part("extra_info") extra_info : String,
        @Part image_file : MultipartBody.Part
    ): Call<CaptureResponse>

    @Multipart
    @POST("app/vibe/capture-from-gallery")
    fun postOnlyImageCapture(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Part("member_id") member_id : Long,
        @Part image_file : MultipartBody.Part
    ): Call<CaptureResponse>

    @POST("app/vibe/capture-without-image")
    fun postWithoutImage(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Body JsonBody : withoutimagedata

    ): Call<CaptureResponse>


    @DELETE("app/album/vibe/{vibe_id}")
    fun deletephoto(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path ("vibe_id") vibe_id: Long?

    ): Call<DeleteResponse>


//    @GET("app/album/vibe/{member_id}")
//    fun getallphoto(
//        @Header("X-AUTH-TOKEN") jwt: String,
//        @Path ("member_id") member_id: Long
//
//    ): Call <GetphotoResponse>


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




