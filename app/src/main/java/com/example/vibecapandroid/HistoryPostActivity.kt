package com.example.vibecapandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.vibecapandroid.databinding.ActivityHistoryPostBinding

class HistoryPostActivity : AppCompatActivity() {
    private val viewBinding : ActivityHistoryPostBinding by lazy {
        ActivityHistoryPostBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histoty_post)



        // 게시물 내용 서버에 전달하기
    }
}