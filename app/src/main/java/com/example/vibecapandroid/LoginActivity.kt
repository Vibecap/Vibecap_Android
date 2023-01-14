package com.example.vibecapandroid

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayoutStates.TAG
import com.example.vibecapandroid.ApiClass
import com.example.vibecapandroid.databinding.ActivityRegisterEmailBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val id = findViewById<Button>(R.id.activity_login_email) as EditText
        val password = findViewById<Button>(R.id.activity_login_password) as EditText
        val btn_login = findViewById<Button>(R.id.activity_login_loginbtn) as Button
        val btn_reg=findViewById<Button>(R.id.activity_login_registerbtn) as TextView



        
        btn_login.setOnClickListener(View.OnClickListener {
            val idStr=id.text.toString()
            val pwStr=password.text.toString()
           /* service.login(idStr,pwStr).enqueue(object :Callback<String>{

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val result =response.body()
                    Log.e("로그인","${result}")
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("로그인","${t.localizedMessage}")

                }

            })*/

            var editor=getSharedPreferences("sharedprefs",Context.MODE_PRIVATE).edit()
            editor.putBoolean("isLoggedIn",true)
            editor.apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        })

        btn_reg.setOnClickListener() {
            val intent = Intent(this, RegisterEmailActivity::class.java)
            startActivity(intent)
            finish()
        }

        //이 부분까지임



    }

    // 로그인 성공/실패 시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        var dialog = AlertDialog.Builder(this)

        if(type.equals("success")){
            dialog.setTitle("로그인 성공")
            dialog.setMessage("로그인 성공!")
        }
        else if(type.equals("fail")){
            dialog.setTitle("로그인 실패")
            dialog.setMessage("아이디와 비밀번호를 확인해주세요")
        }

        var dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()

    }
}

