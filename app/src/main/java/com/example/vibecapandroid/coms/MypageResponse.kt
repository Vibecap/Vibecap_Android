package com.example.vibecapandroid.coms

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

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
data class patchMypageImgInput(
    @SerializedName("member_id")
    val member_id: Long,
    @SerializedName("profile_image")
    val profile_image:MultipartBody.Part
)
data class patchMypageQuitInput(
    @SerializedName("memberID")
    val memberID : Long
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