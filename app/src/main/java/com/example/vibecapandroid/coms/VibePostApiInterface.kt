package com.example.vibecapandroid.coms

import retrofit2.Call
import retrofit2.http.*

// 게시물 전체 조회
interface VibePostAllInterface {
    @GET("app/posts")
    fun postAllCheck(
        @Query("tagName") tagName: String
    ): Call<PostAllResponse>
}

interface VibePostApiInterface {
    // 게시물 1개 조회
    @GET("app/posts/{post_id}")
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
    @DELETE("app/posts/{postId}")
    fun postDelete(
        @Path("postId") postId: Int
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

//interface VibePostWriteInterface {
//
//}
//
//interface VibePostModifyInterface {
//
//}
//
//interface VibePostDeleteInterface {
//
//}