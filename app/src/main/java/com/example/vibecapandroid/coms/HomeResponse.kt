package com.example.vibecapandroid.coms



data class CaptureResponse(
    val is_success : Boolean,
    val code : Int,
    val message : String,
    val result : Result
)

data class Result(
    val youtube_link : String,
    val video_id : String,
    val vibe_id : Long
)


data class DeleteResponse(
    val is_success : Boolean,
    val code : Int,
    val message : String,
    val result : String
)

