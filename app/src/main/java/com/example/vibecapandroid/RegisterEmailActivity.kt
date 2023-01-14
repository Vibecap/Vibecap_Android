package com.example.vibecapandroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
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
            .baseUrl("https://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/app/sign-api/email/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService=retrofit.create(ApiClass::class.java)
        apiService.getEmailSameCheck(viewBinding.activityRegisterEmailSet.toString()).enqueue(object: Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        Log.d(
                            "Retrofit",
                            "Response\nCode: ${responseData.code} Message:${responseData.meesage}"
                        )
                    }
                } else {
                    Log.d("Retrofit", "Resoponse not successful ${response.code()}")
                }
            }
            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.e("Retrofir","Error",t)
            }

        })

        //입력한 주소 중에 하나로 연결시도


    }
}