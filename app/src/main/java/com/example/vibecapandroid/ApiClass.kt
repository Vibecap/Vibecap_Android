package com.example.vibecapandroid

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.vibecapandroid.*


interface ApiClass {
    @GET("app/sign-api/email/{email}")
    fun getEmailSameCheck(
        @Path("email") email:String
    ): Call<Response>

}
