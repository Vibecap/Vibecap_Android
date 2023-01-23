package com.example.vibecapandroid.coms

import com.google.gson.annotations.SerializedName
import retrofit2.Call


/*
-각자 맡은 부분대로
1.LoginApiInterface
-Response data class는 LoginResponse
함수이름은 get,post,patch+기능이름
ex.)getCheckEmailSame
dataclass 이름은 get 뺴고 interface 함수 이름
ex.)CheckEmailSameResponse

2.MypageApiInterface
3.Vibe
4.Capture

 */
data class EmailSameCheckResponse (
    val is_success:Boolean,
    val code:Int,
    val message:String,
    val result:Boolean

)

data class NicknameSameCheckResponse (
    val is_success:Boolean,
    val code:Int,
    val message:String,
    val result:Boolean

)

data class SignUpResponse(
    val is_success:Boolean,
    val code:Int,
    val message:String,
    val result: SignUpMemberID
)

data class SignUpMember(
    @SerializedName("email")
    val email:String,
    @SerializedName("password")
    val password:String,
    @SerializedName("nickname")
    val nickname:String
)

data class SignUpMemberID(
    val member_id :Long
)

data class SignInResponse(
    val is_success:Boolean,
    val code:Int,
    val message:String,
    val result: SignInMemberID
)

data class SignInMember(
    @SerializedName("email")
    val email:String,
    @SerializedName("password")
    val password:String
)

data class SignInMemberID(
    val token:String,
    val nickname :String,
    val member_id :String
)