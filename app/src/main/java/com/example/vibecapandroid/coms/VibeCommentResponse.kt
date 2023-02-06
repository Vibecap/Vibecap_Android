package com.example.vibecapandroid.coms

import com.google.gson.annotations.SerializedName

// 댓글 & 대댓글 조회
data class GetCommentsResponse(
    @SerializedName("is_success") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<CommentsResult>
)

data class CommentsResult(
    @SerializedName("comment_id") val commentId: Long,
    @SerializedName("comment_body") val commentBody: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profile_image") val profileImg: String,
    @SerializedName("sub_comment") val subComment: ArrayList<SubCommentResult>,
    @SerializedName("createdDate") val createdDate: String
)

data class SubCommentResult(
    @SerializedName("sub_comment_id") val subCommentId: Long,
    @SerializedName("comment_id") val commentId: Long,
    @SerializedName("sub_comment_body") val subCommentBody: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profile_image") val profileImg: String,
    @SerializedName("createdDate") val createdDate: String
)

// 댓글 작성
data class WriteCommentResponse(
    @SerializedName("is_success") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: WriteCommentResult
)

data class WriteCommentResult(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("comment_id") val commentId: Long,
    @SerializedName("comment_body") val commentBody: String,
    @SerializedName("member_id") val memberId: Long,
    @SerializedName("profile_image") val profileImg: String
)

// 대댓글 작성
data class WriteSubCommentResponse(
    @SerializedName("is_success") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: WriteSubCommentResult
)

data class WriteSubCommentResult(
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("sub_comment_id") val subCommentId: Long,
    @SerializedName("comment_id") val commentId: Long,
    @SerializedName("sub_comment_body") val subCommentBody: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profile_image") val profileImg: String
)

//** Request **//

data class WriteCommentReq(
    @SerializedName("member_id") val memberId: Long,
    @SerializedName("comment_body") val commentBody: String
)

data class WriteSubCommentReq(
    @SerializedName("member_id") val memberId: Long,
    @SerializedName("sub_comment_body") val subCommentBody: String
)

