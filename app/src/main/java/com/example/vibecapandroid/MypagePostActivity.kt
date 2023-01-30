package com.example.vibecapandroid

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.vibecapandroid.databinding.ActivityVibePostBinding

class MypagePostActivity : AppCompatActivity() {

    val viewBinding : ActivityVibePostBinding by lazy {
        ActivityVibePostBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_post)
        setContentView(viewBinding.root)



    }
}