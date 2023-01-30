package com.example.vibecapandroid.coms

import android.telecom.Call
import android.widget.ImageView
import okhttp3.MultipartBody
import retrofit2.http.*

interface MypageApiInterface {
    @GET("/app/my-page/{member_id}")
    fun getMypageCheck(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path("member_id") member_id:Long
    ): retrofit2.Call<CheckMypageResponse>

    @PATCH("/app/my-page/profile-image")
    fun patchProfileimgChange(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path("profile_image") profile_image:String
    ):retrofit2.Call<CheckMypageResponse>

    @PATCH("app/my-page/setting/sync-gmail")
    fun patchGoogleCheck(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path("google_email") google_email:String
    ):retrofit2.Call<CheckMypageResponse>

    @GET("app/my-page/posts/{member_id}")
    fun getWritedCheck(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path("member_id") member_id: Long
    ):retrofit2.Call<CheckMypageWritedResponse>

    @GET("app/my-page/likes/{member_id}")
    fun getLikeCheck(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path("member_id") member_id: Long
    ):retrofit2.Call<CheckMypageLikeResponse>

    @GET("app/my-page/scraps/{member_id}")
    fun getScrapCheck(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path("member_id") member_id: Long
    ):retrofit2.Call<CheckMypageScrapResponse>


    @PATCH("/app/member/nickname")
    fun patchNicknameChange(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Body JsonBody: patchNickNameInput
    ):retrofit2.Call<ChangeNicknameResponse>

    @Multipart
    @PATCH("/app/my-page/profile-image")
    fun patchMypageImgChange(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Body JsonBody: patchMypageImgInput
    ): retrofit2.Call<patchMypageImgResponse>

    @PATCH("/app/member/quit")
    fun patchMypageQuit(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Body JsonBody : patchMypageQuitInput
    ):retrofit2.Call<patchMypageQuitResponse>
}