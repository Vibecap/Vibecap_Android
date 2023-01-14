package com.example.vibecapandroid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vibecapandroid.databinding.ActivityRegisterPasswordBinding

class RegisterPasswordActivity:AppCompatActivity() {
    private val viewBinding: ActivityRegisterPasswordBinding by lazy {
        ActivityRegisterPasswordBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        viewBinding.activityRegisterPasswordNext.setOnClickListener(){
            val intent = Intent(this, RegisterNicknameActivity::class.java)
            startActivity(intent)
        }
    }
}