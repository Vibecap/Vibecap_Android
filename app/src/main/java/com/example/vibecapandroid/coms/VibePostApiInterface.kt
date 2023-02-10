package com.example.vibecapandroid.coms

import retrofit2.Call
import retrofit2.http.*


// 게시물 전체 조회(태그별)
interface VibePostTagInterface{
    @GET("app/posts")
    fun postAllCheck(
        @Query("tagName") tagName:String
    ): Call<PostTagResponse>
}

// 게시물 전체 조회(weekly)
interface VibePostWeeklyInterface{
    @GET("app/posts/weekly")
    fun postWeeklyCheck(
    ): Call<PostWeeklyResponse>
}


interface VibePostApiInterface {
    // 게시물 전체 조회 (태그별) - 검색
    @GET("app/posts")
    fun postAllCheckWithTag(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Query("tagName") tagName:String,
        @Query("page") page: Int
    ): Call<PostTagResponse>

    // 게시물 전체 조회 (태그 X)
    @GET("/app/posts")
    fun postAllCheck(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Query("page") page: Int
    ): Call<PostTagResponse>

    // 게시물 1개 조회
    @GET("/app/posts/{post_id}")
    fun postDetailCheck(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path("post_id") postId: Int,
        @Query("memberId") memberId : Long
    ): Call<PostDetailResponse>

    // 게시물 작성
    @POST("app/posts")
    fun postWrite(
        @Body JsonBody: PostMember
    ): Call<PostWriteResponse>

    // 게시물 수정
    @PATCH("app/posts/{postIdx}")
    fun postModify(

    )

    // 게시물 삭제
    @HTTP(method = "DELETE", path = "http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/app/posts/{post_id}", hasBody = true)
    fun postDelete(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path("post_id") postId: Int,
        @Body memberId: MemberId
    ): Call<PostDeleteResponse>

    // 좋아요
    @POST("/app/posts/{post_id}/like")
    fun postLike(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path("post_id") postId: Int,
        @Body memberId: MemberId
    ): Call<PostLikeResponse>

    // 스크랩
    @POST("/app/posts/{post_id}/scrap")
    fun postScrap(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path("post_id") postId: Int,
        @Body memberId: MemberId
    ): Call<PostScrapResponse>

}