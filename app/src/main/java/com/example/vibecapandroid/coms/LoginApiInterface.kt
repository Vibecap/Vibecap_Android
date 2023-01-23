package com.example.vibecapandroid.coms

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.*
import com.example.vibecapandroid.*
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.POST

//함수 이름 camel case로
//ex. getCheckEmailSame

interface LoginApiInterface {
    @GET("app/sign-api/email/{email}")
    fun getEmailSameCheck(
        @Path("email") email:String
    ): Call<EmailSameCheckResponse>

    @GET("app/sign-api/nickname/{nickname}")
    fun getNicknameSameCheck(
        @Path("nickname") nickname:String
    ): Call<NicknameSameCheckResponse>

    @POST("app/sign-api/sign-up")
    fun postSignUpOwn(
        @Body JsonBody: SignUpMember
    ): Call<SignUpResponse>

    @POST("app/sign-api/sign-in")
    fun postSignInOwn(
        @Body JsonBody: SignInMember
    ): Call<SignInResponse>
}