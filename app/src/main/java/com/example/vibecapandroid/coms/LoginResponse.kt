package com.example.vibecapandroid.coms


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
data class CheckEmailResponse (
    val is_success:Boolean,
    val code:Int,
    val message:String,
    val result:Boolean

)

