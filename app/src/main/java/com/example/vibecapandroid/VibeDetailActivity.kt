package com.example.vibecapandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.vibecapandroid.databinding.ActivityVibeDetailBinding


class VibeDetailActivity : AppCompatActivity() {
    val viewBinding : ActivityVibeDetailBinding by lazy {
        ActivityVibeDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vibe_detail)

        val toolbar = findViewById<Toolbar>(R.id.toolBar_top)
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 게시물 작성 열기
        viewBinding.btnAddpost.setOnClickListener {
            val intent = Intent(this, HistoryPostActivity::class.java)
            startActivity(intent)
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