package com.example.vibecapandroid.coms

import com.google.gson.annotations.SerializedName
import java.sql.Blob
import java.time.LocalDateTime

// 게시물 전체 조회
data class PostAllResponse(
    @SerializedName("is_success") val is_success: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: List<PostAllData>
)

data class PostAllData(
    @SerializedName("post_id") val post_id: Int,
    @SerializedName("member_id") val member_id: Blob,
    @SerializedName("vibe_id") val vibe_id: Int,
    @SerializedName("vibe_img") val vibe_img: String
)

// 게시물 개별 조회
data class PostDetailResponse(
    @SerializedName("is_success") val is_success: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: PostDetailData
)

data class PostDetailData(
    @SerializedName("post_id") val postId: Int,
    @SerializedName("member_id") val memberId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("vibe_id") val vibeId: Int,
    @SerializedName("vibe_image") val vibeImg: String,
    @SerializedName("youtube_link") val youtubeLink: String,
    @SerializedName("like_number") val likeNumber: Int,
    @SerializedName("scrap_number") val scrapNumber: Int,
    @SerializedName("comment_number") val commentNumber: Int,
    @SerializedName("tag_name") val tagName: String,
    @SerializedName("profile_image") val profileImg: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("modified_date") val modifiedDate: String
)

// 게시물 작성
data class PostWriteResponse(
    val is_success: Boolean,
    val code: Int,
    val message: String,
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
    val is_success: Boolean,
    val code: Int,
    val message: String,
    val result: Char
)


// 게시물 좋아요
data class PostLikeResponse(
    val is_success: Boolean,
    val code: Int,
    val message: String,
    val result: IsPostLiked
)

data class IsPostLiked(
    @SerializedName("like_or_else") val likeOrElse: String
)

// 게시물 스크랩
data class PostScrapResponse(
    val is_success: Boolean,
    val code: Int,
    val message: String,
    val result: IsPostScraped
)

data class IsPostScraped(
    @SerializedName("scrap_or_else") val scrapOrElse: String
)


//** Request **//

data class MemberId(
    @SerializedName("member_id") val memberId: Long
)