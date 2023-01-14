package com.example.vibecapandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.vibecapandroid.coms.CheckEmailResponse
import com.example.vibecapandroid.coms.LoginApiInterface
import com.example.vibecapandroid.databinding.ActivityRegisterEmailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterEmailActivity:AppCompatActivity() {
    private val viewBinding: ActivityRegisterEmailBinding by lazy{
        ActivityRegisterEmailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.activityRegisterEmailNext.setOnClickListener(){

            val intent = Intent(this, RegisterPasswordActivity::class.java)
            startActivity(intent)
        }

        //이 부분부터 Retrofit 실습하는것
        val retrofit= Retrofit.Builder()
            .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //base url 설정

        val apiService=retrofit.create(LoginApiInterface::class.java)
        apiService.getEmailSameCheck(viewBinding.activityRegisterEmailSet.text.toString()).enqueue(object: Callback<CheckEmailResponse> {
            override fun onResponse(call: Call<CheckEmailResponse>, response: retrofit2.Response<CheckEmailResponse>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        Log.d(
                            "Retrofit",
                            "Response\n" +
                                    "isSuccess:${responseData.is_success} " +
                                    "Code: ${responseData.code} " +
                                    "Message:${responseData.message} " +
                                    "Result:${responseData.result}"
                        )
                    }
                    else{
                        Log.d("Retrofit","Null data")
                    }
                }
                else {
                    Log.d("Retrofit", "Response not successful ${response.code()}")
                }
            }
            override fun onFailure(call: Call<CheckEmailResponse>, t: Throwable) {
                Log.e("Retrofit","Error",t)
            }

        })

        //입력한 주소 중에 하나로 연결시도


    }
}