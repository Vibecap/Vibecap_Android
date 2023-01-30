package com.example.vibecapandroid.coms

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApiInterface {
    @POST("app/posts")
    fun posting(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Body JsonBody : PostRequest
        ): Call<PostResponse>

}

data class PostRequest(
    val member : member,
    val title : String,
    val body : String, //게시물 내용
    val vibe : vibe, //capture 한 사진
    val tag_name : String //게시물 태그내용
)

data class member(
    val memberId : Int
)
data class vibe(
    val vibeId  : Int
)