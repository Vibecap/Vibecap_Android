package com.example.vibecapandroid

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayoutStates.TAG

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btn_login = findViewById<Button>(R.id.btn_login) as Button
        btn_login.setOnClickListener {

            //editText로부터 입력된 값을 받아온다


            val id = R.id.et_id.toString()
            val pw = R.id.et_pass.toString()

            // 쉐어드로부터 저장된 id, pw 가져오기
            val sharedPreference = getSharedPreferences("file name", MODE_PRIVATE)
            val savedId = sharedPreference.getString("id", "")
            val savedPw = sharedPreference.getString("pw", "")

            // 유저가 입력한 id, pw값과 쉐어드로 불러온 id, pw값 비교
            if(id == savedId && pw == savedPw){
                // 로그인 성공 다이얼로그 보여주기
                dialog("success")

                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
            else{
                // 로그인 실패 다이얼로그 보여주기
                dialog("fail")
            }
        }

        btn_login.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })


        // 회원가입 버튼

        val btn_register = findViewById<Button>(R.id.btn_register)
        btn_register.setOnClickListener(View.OnClickListener {

            //아직 RegisterActivity 안만듬 (회원가입)
            //val intent = Intent(this, Register::class.java)
            //startActivity(intent)
        })

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