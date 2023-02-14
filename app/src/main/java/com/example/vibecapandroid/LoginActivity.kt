package com.example.vibecapandroid

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayoutStates.TAG
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import com.example.vibecapandroid.coms.LoginApiInterface
import com.example.vibecapandroid.coms.SignInMember
import com.example.vibecapandroid.coms.SignInMemberID
import com.example.vibecapandroid.coms.SignInResponse
import com.example.vibecapandroid.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {
    private val viewBinding: ActivityLoginBinding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
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

        val apiService = retrofit.create(LoginApiInterface::class.java)

        viewBinding.activityLoginLoginbtn.setOnClickListener {
            val idStr = viewBinding.activityLoginEmail.text.toString()
            val pwStr = viewBinding.activityLoginPassword.text.toString()

            apiService.postSignInOwn(SignInMember(idStr, pwStr))
                .enqueue(object : Callback<SignInResponse> {
                    override fun onResponse(
                        call: Call<SignInResponse>,
                        response: Response<SignInResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseData = response.body()
                            if (responseData != null) {
                                Log.d(
                                    "TAG",
                                    "RegisterResponse\n" +
                                            "isSuccess:${responseData.is_success} " +
                                            "Code: ${responseData.code} " +
                                            "Message:${responseData.message} " +
                                            "Result:${responseData.result}"
                                )
                                if (responseData.is_success) {
                                    var editor = getSharedPreferences(
                                        "sharedprefs",
                                        Context.MODE_PRIVATE
                                    ).edit()
                                    editor.putBoolean("isLoggedIn", true)
                                    editor.putString("Token", responseData.result.token)
                                    editor.putLong("Member_Id",responseData.result.member_id.toLong())
                                    editor.apply()
                                    val intent =
                                        Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    if (responseData.code == 3201) {
                                        viewBinding.activityLoginEmail812.setTextColor(Color.RED)
                                        viewBinding.activityLoginEmail812.text =
                                            responseData.message
                                        viewBinding.activityLoginPasswordAgaintype.setTextColor(
                                            Color.BLACK
                                        )
                                        viewBinding.activityLoginPasswordAgaintype.text = ""
                                    } else if (responseData.code == 3202) {
                                        viewBinding.activityLoginEmail812.setTextColor(Color.BLACK)
                                        viewBinding.activityLoginEmail812.text = ""
                                        viewBinding.activityLoginPasswordAgaintype.setTextColor(
                                            Color.RED
                                        )
                                        viewBinding.activityLoginPasswordAgaintype.text =
                                            responseData.message
                                    }
                                }
                            } else
                                Log.d("Login", "Null data")
                        } else
                            Log.d("Login", "Response not success")
                    }

                    override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                    }
                })


        }

        viewBinding.activityLoginEmail.addTextChangedListener(){
            viewBinding.activityLoginEmail812.text=""
        }
        viewBinding.activityLoginPassword.addTextChangedListener(){
            viewBinding.activityLoginPasswordAgaintype.text=""
        }
        viewBinding.activityLoginRegisterbtn.setOnClickListener() {
            val intent = Intent(this@LoginActivity, RegisterEmailActivity::class.java)
            startActivity(intent)
        }
    }
    private var waitTime = 0L
    override fun onBackPressed() {
        if(System.currentTimeMillis() - waitTime >=1500 ) {
            waitTime = System.currentTimeMillis()
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            //앱종료
            ActivityCompat.finishAffinity(this)
            System.exit(0)
        }
    }
}