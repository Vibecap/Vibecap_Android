package com.example.vibecapandroid.coms

import java.util.Objects

data class CaptureResponse(

    val is_success : Boolean,
    val code : Int,
    val message : String,
    val CaptureResult : CaptureResult
)

data class CaptureResult(
    val label : String,
    val youtube_lnk : String,
    val video_Id : String
)





