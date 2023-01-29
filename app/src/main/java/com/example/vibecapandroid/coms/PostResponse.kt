package com.example.vibecapandroid.coms

data class PostResponse(
    val is_success: Boolean,
    val code : Int,
    val message : String,
    val result : Int //post id 값
)
