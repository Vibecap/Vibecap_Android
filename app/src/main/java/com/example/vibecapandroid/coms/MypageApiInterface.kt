package com.example.vibecapandroid.coms

import android.telecom.Call
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface MypageApiInterface {
    @GET("app/my-page")
    fun getMypageCheck(
        @Path("member_id") member_id:Long
    ): retrofit2.Call<CheckMypageResponse>

    @PATCH("app/my-page/profile-image")
    fun patchProfileimgChange(
        @Path("profile_image") profile_image:String
    ):retrofit2.Call<CheckMypageResponse>

    @PATCH("app/my-page/setting/sync-gmail")
    fun patchGoogleCheck(
        @Path("google_email") google_email:String
    ):retrofit2.Call<CheckMypageResponse>

    @GET("app/my-page/posts")
    fun getWritedCheck(
        @Path("member_id") member_id: Long
    ):retrofit2.Call<CheckMypageResponse>

}