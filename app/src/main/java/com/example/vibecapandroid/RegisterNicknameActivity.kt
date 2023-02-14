package com.example.vibecapandroid

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast.LENGTH_LONG
import androidx.core.widget.addTextChangedListener
import com.example.vibecapandroid.coms.LoginApiInterface
import com.example.vibecapandroid.coms.NicknameSameCheckResponse
import com.example.vibecapandroid.coms.SignUpMember
import com.example.vibecapandroid.coms.SignUpResponse
import com.example.vibecapandroid.databinding.ActivityRegisterNicknameBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterNicknameActivity : AppCompatActivity() {
    private val viewBinding: ActivityRegisterNicknameBinding by lazy{
        ActivityRegisterNicknameBinding.inflate(layoutInflater)
    }

    //이 부분부터 Retrofit 실습하는것
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    //base url 설정


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        val email = intent.extras!!.getString("Email") /*String형*/
        val password=intent.extras!!.getString("Password")
        Log.d("Email","$email")
        Log.d("Password","$password")
        var boxCheck=false
        var nicknameCheck=false

        viewBinding.activityRegisterNicknameBack.setOnClickListener(){
            finish()
        }

        viewBinding.activityRegisterNicknameNext.isEnabled=false
        fun Register(){
            if(boxCheck&&nicknameCheck) {
                if(viewBinding.activityRegisterNicknameSet.text.toString()==""){
                    viewBinding.activityRegisterNicknameNext.setTextColor(Color.GRAY)
                    viewBinding.activityRegisterNicknameNext.isEnabled=false
                    viewBinding.activityRegisterNickname812.text="닉네임을 입력해주세요"
                    nicknameCheck = false
                }
                else {
                    viewBinding.activityRegisterNicknameNext.setTextColor(Color.BLACK)
                    viewBinding.activityRegisterNicknameNext.isEnabled = true
                }
            }
            else{
                viewBinding.activityRegisterNicknameNext.setTextColor(Color.GRAY)
                viewBinding.activityRegisterNicknameNext.isEnabled=false
            }
        }

        viewBinding.activityRegisterNicknameCheckBoxall.setOnClickListener{
            when {
                viewBinding.activityRegisterNicknameCheckBoxall.isChecked-> {
                    viewBinding.activityRegisterNicknameCheckBoxprivate.isChecked = true
                    viewBinding.activityRegisterNicknameCheckBoxrother.isChecked = true
                    viewBinding.activityRegisterNicknameCheckBoxuse.isChecked = true
                    boxCheck=true
                    Register()
                }
                else->{
                    viewBinding.activityRegisterNicknameCheckBoxprivate.isChecked = false
                    viewBinding.activityRegisterNicknameCheckBoxrother.isChecked = false
                    viewBinding.activityRegisterNicknameCheckBoxuse.isChecked = false
                    boxCheck=false
                }
            }
        }

        val apiService=retrofit.create(LoginApiInterface::class.java)

        viewBinding.activityRegisterNicknameSet.addTextChangedListener() {
            if(viewBinding.activityRegisterNicknameSet.text.toString()==""){
                viewBinding.activityRegisterNickname812.text="닉네임을 입력해주세요"
                nicknameCheck = false
                Register()
            }
            else {
                apiService.getNicknameSameCheck(viewBinding.activityRegisterNicknameSet.text.toString())
                    .enqueue(object : Callback<NicknameSameCheckResponse> {
                        override fun onResponse(
                            call: Call<NicknameSameCheckResponse>,
                            response: Response<NicknameSameCheckResponse>
                        ) {
                            if (response.isSuccessful) {
                                val responseData = response.body()
                                if (responseData != null) {
                                    if (responseData.is_success) {
                                        if (responseData.result) {
                                            viewBinding.activityRegisterNickname812.text =
                                                "이미 존재 하는 닉네임 입니다."
                                            nicknameCheck = false
                                        } else {
                                            viewBinding.activityRegisterNickname812.text =
                                                "사용 가능 한 닉네임 입니다."
                                            nicknameCheck = true
                                            Register()
                                        }
                                    }
                                } else {
                                    Log.d("RetrofitCheck emailsmae", "Null data")
                                }
                            } else {
                                Log.d(
                                    "Retrofitcheck email same",
                                    "NickNameSame Response not successful ${response.code()}"
                                )
                            }
                        }

                        override fun onFailure(
                            call: Call<NicknameSameCheckResponse>,
                            t: Throwable
                        ) {
                            Log.e("Retrofit", "Error", t)
                        }

                    })
            }
        }

        viewBinding.activityRegisterNicknameNext.setOnClickListener{
            apiService.postSignUpOwn(SignUpMember(email!!,password!!, viewBinding.activityRegisterNicknameSet.text.toString()))
                .enqueue(object :Callback<SignUpResponse>{
                    override fun onResponse(
                        call: Call<SignUpResponse>,
                        response: Response<SignUpResponse>
                    ) {
                        if(response.isSuccessful){
                            val responseData=response.body()
                            if(responseData!=null){
                                if(responseData.is_success&&viewBinding.activityRegisterNicknameNext.isEnabled){
                                    val intent = Intent(this@RegisterNicknameActivity,LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                else{
                                    Log.d(
                                        "Retrofit",
                                        "RegisterResponse\n" +
                                                "isSuccess:${responseData.is_success} " +
                                                "Code: ${responseData.code} " +
                                                "Message:${responseData.message} " +
                                                "Result:${responseData.result}"
                                    )
                                }
                            }
                            else {
                                Log.d("Retrofit", "Null data")
                            }
                        } else {
                            Log.d("Retrofit", "SignUpResponse not successful ${response.code()}")
                        }
                    }
                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                        Log.d("Retrofit","Error")
                    }

                })
        }


    }
}