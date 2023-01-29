package com.example.vibecapandroid.coms

import android.net.Uri
import com.google.gson.annotations.SerializedName
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.Base64


data class HistoryAllResponse(
    val is_success:Boolean,
    val code:Int,
    val message:String,
    val result: HistoryAllResult
)

data class HistoryAllResult(
    val nickname :String,
    val email :String,
    val google_email:String,
    val album:List<HistoryMainImageClass>
)

data class HistoryMainImageClass(
    val vibe_id:Long,
    val vibe_image: String
)

//찍은 사진 중 하나만 조회하는것
data class HistoryOneResponse(
    val is_success: Boolean,
    val code :Int,
    val message: String,
    val result : HistoryOneResult
)

data class HistoryOneResult(
    val vibe_id:Long,
    val member_id:Long,
    val vibe_image:String,
    val youtube_link:String,
    val vibe_keywords:String
)

//해당 사진을 Posting 하는 부분
data class HistoryPostingBody(
    val member: memberIdClass,
    val title : String,
    val body : String,
    val vibe : vibeIDClass,
    val tag_name: String
)
data class memberIdClass(
    val member_id: Long
)

data class vibeIDClass(
    val tag_name:String
)

data class HistoryPostingResponse(
    val is_success: Boolean,
    val code:Int,
    val message:String,
    val result:Int
)