package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar

class VibeSearchActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        // 툴바
        val toolbar = findViewById<Toolbar>(R.id.toolBar_top)
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        val searchBtn = findViewById<ImageButton>(R.id.imageButton_tagsearch)
        searchBtn.setOnClickListener {
           // 검색 API 추가
            // 검색 결과 출력
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> {}
        }

        return super.onOptionsItemSelected(item)
    }
}