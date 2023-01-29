package com.example.vibecapandroid.coms

import com.google.gson.annotations.SerializedName
import java.sql.Blob
import java.sql.Timestamp

// 게시물 전체 조회
data class PostAllResponse(
    val is_success:Boolean,
    val code:Int,
    val message:String,
    val data: PostAllData,
)
data class PostAllData(
    val post_id: Int,
    val vibe_id: Int,
    val img_id: Int,
    val youtube_link:Char,
    val member_id: Int
)

// 게시물 개별 조회

data class PostDetailResponse(
    @SerializedName("is_success") val is_success: Boolean,
    @SerializedName("code")val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result")val result: List<PostDetailData>
)
data class PostDetailData(
    @SerializedName("post_id") val post_id: Int,
    @SerializedName("member_id") val member_id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body : String,
    @SerializedName("vibe_id") val vibe_id: Int,
    @SerializedName("vibe_image") val vibe_image: String,
    @SerializedName("like_number") val like_number: Int,
    @SerializedName("scrap_number") val scrap_number: Int,
    @SerializedName("comment_number") val comment_number: Int,
    @SerializedName("tag_name") val tag_name: String,
    @SerializedName("profileImg") val profileImg: String,
    @SerializedName("nickname") val nickname : String
)

// 게시물 작성
data class PostWriteResponse(
    val is_success:Boolean,
    val code:Int,
    val message:String,
    val result: PostMember
)

data class PostMember(
    @SerializedName("member_id")
    val member_id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: Char,
    @SerializedName("vibe_id")
    val vibe_id: Int,
    @SerializedName("tag_name")
    val tag_name: Char
)

// 게시물 수정

// 게시물 삭제
data class PostDeleteResponse(
    val is_success:Boolean,
    val code:Int,
    val message:String,
    val result: Char
)