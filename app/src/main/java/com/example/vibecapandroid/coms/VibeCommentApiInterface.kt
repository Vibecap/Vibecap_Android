package com.example.vibecapandroid.coms

import retrofit2.Call
import retrofit2.http.*

interface VibeCommentApiInterface {

    // 댓글 & 대댓글 조회
    @GET("/app/comments/{post_id}")
    fun getComments(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path("post_id") postId: Int,
    ): Call<GetCommentsResponse>

    // 댓글 작성
    @POST("/app/comments/{post_id}")
    fun writeComment(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path("post_id") postId: Int,
        @Body writeCommentReq: WriteCommentReq
    ): Call<WriteCommentResponse>

    // 댓글 삭제
    @DELETE("/app/comments/{post_id}/{comment_id}")
    fun deleteComment(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path("post_id") postId: Long,
        @Path("comment_id") commentId: Long,
        @Body memberId: Long
    )

    // 대댓글 작성
    @POST("/app/sub/comments/{comment_id}")
    fun writeSubComment(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path("comment_id") commentId: Int,
        @Body writeSubCommentReq: WriteSubCommentReq
    ): Call<WriteSubCommentResponse>

    // 대댓글 삭제
    @DELETE("/app/sub/comments/{sub_comment_id}")
    fun deleteSubComment(
        @Header("X-AUTH-TOKEN") jwt: String,
        @Path("sub_comment_id") subCommentId: Long,
    )
}