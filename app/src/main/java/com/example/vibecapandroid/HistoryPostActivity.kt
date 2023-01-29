package com.example.vibecapandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vibecapandroid.coms.HistoryApiInterface

class HistoryPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histoty_post)

        val apiService = retrofit.create(HistoryApiInterface::class.java)
    }
}