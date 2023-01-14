package com.example.vibecapandroid.coms

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.vibecapandroid.*

//함수 이름 camel case로
//ex. getCheckEmailSame

interface LoginApiInterface {
    @GET("app/sign-api/email/{email}")
    fun getEmailSameCheck(
        @Path("email") email:String
    ): Call<CheckEmailResponse>

}