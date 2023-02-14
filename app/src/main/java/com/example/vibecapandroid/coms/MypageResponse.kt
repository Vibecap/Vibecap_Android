package com.example.vibecapandroid.coms

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class CheckMypageResponse(
    val is_success: Boolean,
    val code: Int,
    val message: String,
    val result: CheckMypageResponseResult
)
data class CheckMypageResponseResult(
    val member_id: Long,
    val email: String,
    val google_email: String,
    val nickname: String,
    val profile_image: String,
)
data class CheckMypageWritedResponse(
    val is_success: Boolean,
    val code: Int,
    val message: String,
    val result: List<CheckMypageWritedResponseResult>
)
data class CheckMypageWritedResponseResult(
    //게시물 아이디
    val post_id: Long,
    //사진 number
    val vibe_id: Long,
    val vibe_image: String
)

data class CheckMypageLikeResponse(
    val is_success: Boolean,
    val code: Int,
    val message: String,
    val result: List<CheckMypageLikeResponseResult>
)
data class CheckMypageLikeResponseResult(
    val like_id:Long,
    //게시물 아이디
    val post_id: Long,
    //사진 number
    val vibe_id: Long,
    val vibe_image: String
)

data class CheckMypageScrapResponse(
    val is_success: Boolean,
    val code: Int,
    val message: String,
    val result: List<CheckMypageScrapResponseResult>
)
data class CheckMypageScrapResponseResult(
    val scrap_id:Long,
    //게시물 아이디
    val post_id: Long,
    //사진 number
    val vibe_id: Long,
    val vibe_image: String
)


data class ChangeNicknameResponse(
    val is_success: Boolean,
    val code: Int,
    val message: String,
    val result: ChangeNicknameResponseResult
)
data class ChangeNicknameResponseResult(
    val nickname: String
)
data class patchNickNameInput(
    @SerializedName ("member_id")
    val member_id: Long,
    @SerializedName("new_nickname")
    val new_nickname:String
)

data class patchMypageImgResponse(
    val is_success: Boolean,
    val code: Int,
    val message: String,
    val result: String
)
/*
data class patchMypageImgInput(
    @SerializedName("member_id")
    val member_id: String,
    @SerializedName("profile_image")
    val profile_image:MultipartBody.Part
)*/
data class patchMypageQuitInput(
    val member_id : Long
)
data class patchMypageQuitResponse(
    val is_success: Boolean,
    val code: Int,
    val message: String,
    val result: patchMypageQUitresponseResult
)
data class patchMypageQUitresponseResult(
    val nickname: String
)
data class postMypageResponse(
    val is_success: Boolean,
    val code: Int,
    val message: String,
    val result: postMypageResponseResult
)
data class postMypageResponseResult(
    val title: String,
    val body: String,
    val nickname: String,
    val modified_data: String,
    val post_id: Int,
    val member_id: Long,
    val vibe_id: Int,
    val vibe_image: String,
    val like_number: Int,
    val scrap_number: Int,
    val comment_number: Int,
    val tag_name: String,
    val profileImg: String,

    )
data class postMypageInput(
    @SerializedName("post_id") val post_id: Int,
    @SerializedName("member_id") val member_id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("vibe_id") val vibe_id: Int,
    @SerializedName("vibe_image") val vibe_image: String,
    @SerializedName("like_number") val like_number: Int,
    @SerializedName("scrap_number") val scrap_number: Int,
    @SerializedName("comment_number") val comment_number: Int,
    @SerializedName("tag_name") val tag_name: String,
    @SerializedName("profileImg") val profileImg: String,
    @SerializedName("nickname") val nickname : String,
    @SerializedName("modified_date") val modified_data: String
)
data class postMypageLikeResponse(
    val is_success: Boolean,
    val code: Int,
    val message: String,
    val result: List<postMypageLikeResponseResult>
)
data class postMypageLikeResponseResult(
    val title: String,
    val body: String,
    val nickname: String,
    val modified_data: LocalDateTime,
    val post_id: Int,
    val member_id: Long,
    val vibe_id: Int,
    val vibe_image: String,
    val like_number: Int,
    val scrap_number: Int,
    val comment_number: Int,
    val tag_name: String,
    val profileImg: String,

    )
data class postMypageLikeInput(
    @SerializedName("post_id") val post_id: Int,
    @SerializedName("member_id") val member_id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("vibe_id") val vibe_id: Int,
    @SerializedName("vibe_image") val vibe_image: String,
    @SerializedName("like_number") val like_number: Int,
    @SerializedName("scrap_number") val scrap_number: Int,
    @SerializedName("comment_number") val comment_number: Int,
    @SerializedName("tag_name") val tag_name: String,
    @SerializedName("profileImg") val profileImg: String,
    @SerializedName("nickname") val nickname : String,
    @SerializedName("modified_date") val modified_data: LocalDateTime
)
data class postMypageScrapResponse(
    val is_success: Boolean,
    val code: Int,
    val message: String,
    val result: List<postMypageScrapResponseResult>
)
data class postMypageScrapResponseResult(
    val title: String,
    val body: String,
    val nickname: String,
    val modified_data: LocalDateTime,
    val post_id: Int,
    val member_id: Long,
    val vibe_id: Int,
    val vibe_image: String,
    val like_number: Int,
    val scrap_number: Int,
    val comment_number: Int,
    val tag_name: String,
    val profileImg: String,

    )
data class postMypageScrapInput(
    @SerializedName("post_id") val post_id: Int,
    @SerializedName("member_id") val member_id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("vibe_id") val vibe_id: Int,
    @SerializedName("vibe_image") val vibe_image: String,
    @SerializedName("like_number") val like_number: Int,
    @SerializedName("scrap_number") val scrap_number: Int,
    @SerializedName("comment_number") val comment_number: Int,
    @SerializedName("tag_name") val tag_name: String,
    @SerializedName("profileImg") val profileImg: String,
    @SerializedName("nickname") val nickname : String,
    @SerializedName("modified_date") val modified_data: LocalDateTime
)

data class patchMypageEditResponse(
    val is_success: Boolean,
    val code: Int,
    val message: String,
    val result: String

)
data class patchMypageEditPostInput(
    val member: member,
    val title: String?,
    val body: String?
)

data class EditRequest(
    val member : member,
    val title : String?,
    val body : String?
)

data class deleteMypageResponse(
    val is_success: Boolean,
    val code: Int,
    val message: String,
    val result: String

)
data class deleteMypagePostInput(
    val member_id: Int
)

// 활동 내역(알림) 조회
data class GetAlarmHistoryResponse(
    @SerializedName("is_success") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<Notice>
)

data class Notice(
    @SerializedName("notice_id") val noticeId: Long,
    @SerializedName("event") val event: String,
    @SerializedName("post_id") val postId: Long,
    @SerializedName("time") val time: String,
    @SerializedName("sender") val sender: String,
    @SerializedName("summary") val summary: String  // 이벤트가 like인 경우 null 반환
)

// 활동 내역(알림) 읽음 처리
data class DeleteAlarmResponse(
    @SerializedName("is_success") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Long
)