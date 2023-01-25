package com.example.vibecapandroid.coms

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
    val album:List<HistoryImageData>
)

data class HistoryImageData(
    val vibe_id:Long,
    val vibe_image:String
)