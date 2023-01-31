package com.example.vibecapandroid.coms

import retrofit2.Call
import retrofit2.http.*

// 게시물 전체 조회
interface VibePostAllInterface{
    @GET("app/posts")
    fun postAllCheck(
        @Query("tagName") tagName: String
    ): Call<PostAllResponse>
}

// 게시물 1개 조회
interface VibePostApiInterface {
    @GET("app/posts/{post_id}")
    fun postDetailCheck(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path("post_id") postId: Long
    ): Call<PostDetailResponse>
}

// 게시물 작성
interface VibePostWriteInterface{
    @POST("app/posts")
    fun postWrite(
        @Body JsonBody: PostMember
    ): Call<PostWriteResponse>
}

// 게시물 수정
interface VibePostModifyInterface{
    @PATCH("app/posts/{postIdx}")
    fun postModify(

    )
}
// 게시물 삭제
interface VibePostDeleteInterface{
    @DELETE("app/posts/{postId}")
    fun postDelete(
        @Path("postId") postId: Int
    ):Call<PostDeleteResponse>
}