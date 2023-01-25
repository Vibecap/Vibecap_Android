package com.example.vibecapandroid.coms



data class CaptureResponse(
    val is_success : Boolean,
    val code : Int,
    val message : String,
    val result : Result
)

data class Result(
    val youtube_link : String,
    val video_id : String
)

data class WEATHER (
    val response : RESPONSE
)
data class RESPONSE (
    val header : HEADER,
    val body : BODY
)
data class HEADER(
    val resultCode : Int,
    val resultMsg : String
)
data class BODY(
    val dataType : String,
    val items : ITEMS
)
data class ITEMS(
    val item : List<ITEM>
)
data class ITEM(
    val baseData : Int,
    val baseTime : Int,
    val category : String
)

