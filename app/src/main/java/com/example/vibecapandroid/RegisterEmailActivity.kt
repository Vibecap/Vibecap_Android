package com.example.vibecapandroid

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.vibecapandroid.coms.EmailSameCheckResponse
import com.example.vibecapandroid.coms.LoginApiInterface
import com.example.vibecapandroid.databinding.ActivityRegisterEmailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class RegisterEmailActivity:AppCompatActivity() {
    private val viewBinding: ActivityRegisterEmailBinding by lazy{
        ActivityRegisterEmailBinding.inflate(layoutInflater)
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

        val apiService=retrofit.create(LoginApiInterface::class.java)
        val pattern: Pattern = Patterns.EMAIL_ADDRESS

        viewBinding.activityRegisterEmailBack.setOnClickListener(){
            finish()
        }

        viewBinding.activityRegisterEmailSet.addTextChangedListener() {
            if (pattern.matcher(viewBinding.activityRegisterEmailSet.text.toString()).matches()) {
                apiService.getEmailSameCheck(viewBinding.activityRegisterEmailSet.text.toString())
                    .enqueue(object : Callback<EmailSameCheckResponse> {
                        override fun onResponse(
                            call: Call<EmailSameCheckResponse>,
                            response: retrofit2.Response<EmailSameCheckResponse>
                        ) {
                            if (response.isSuccessful) {
                                val responseData = response.body()
                                if (responseData != null) {
                                    if (responseData.is_success) {
                                        if (responseData.result) {
                                            viewBinding.activityRegisterEmailCheck.text =
                                                "이미 존재 하는 이메일 입니다."
                                            viewBinding.activityRegisterEmailNext.setTextColor(Color.GRAY)
                                        } else {
                                            viewBinding.activityRegisterEmailCheck.text =
                                                "사용 가능 한 이메일입니다."
                                            viewBinding.activityRegisterEmailNext.setTextColor(Color.BLACK)
                                            viewBinding.activityRegisterEmailNext.setOnClickListener(){
                                                val intent = Intent(this@RegisterEmailActivity, RegisterPasswordActivity::class.java)
                                                intent.putExtra("Email",viewBinding.activityRegisterEmailSet.text.toString())
                                                startActivity(intent)
                                            }
                                        }
                                    }
                                    Log.d(
                                        "Retrofit",
                                        "EmailChekcSameResponse\n" +
                                                "isSuccess:${responseData.is_success} " +
                                                "Code: ${responseData.code} " +
                                                "Message:${responseData.message} " +
                                                "Result:${responseData.result}"
                                    )
                                } else {
                                    Log.d("Retrofit", "Null data")
                                }
                            } else {
                                Log.d("Retrofit", "Response not successful ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<EmailSameCheckResponse>, t: Throwable) {
                            Log.e("Retrofit", "Error", t)
                        }

                    })
            } else {
                viewBinding.activityRegisterEmailNext.setTextColor(Color.GRAY)
                viewBinding.activityRegisterEmailCheck.text = "이메일 형식이 아닙니다."
            }
        }
    }
}