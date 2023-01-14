package com.example.vibecapandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vibecapandroid.databinding.ActivityRegisterNicknameBinding

class RegisterNicknameActivity : AppCompatActivity() {
    private val viewBinding: ActivityRegisterNicknameBinding by lazy{
        ActivityRegisterNicknameBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.activityRegisterNicknameNext.setOnClickListener()
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}