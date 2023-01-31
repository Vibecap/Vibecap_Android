package com.example.vibecapandroid

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar

class VibeCommentActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vibe_comment)


        // 툴바
//        val toolbar = findViewById<Toolbar>(R.id.toolBar_top)
//        setSupportActionBar(toolbar)
//        val ab = supportActionBar!!
//        ab.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//        // 댓글 popup
//
//        val comment_popup2 = findViewById<ImageButton>(R.id.imageButton_popup)
//        comment_popup2.setOnClickListener {
//            // Dialog만들기
//            val mDialogView = LayoutInflater.from(this).inflate(R.layout.activity_popup_comment, null)
//            val mBuilder = AlertDialog.Builder(this)
//                .setView(mDialogView)
//
//            val mAlertDialog = mBuilder.show()
//
//            val block_table = mDialogView.findViewById<Button>(R.id.block_table)
//            block_table.setOnClickListener {
//
//                Toast.makeText(this, "댓글 작성자를 차단했습니다.", Toast.LENGTH_SHORT).show()
//                // 차단 API
//            }
//
//            val declaration_table = mDialogView.findViewById<Button>(R.id.declaration_table)
//            declaration_table.setOnClickListener {
//
//                Toast.makeText(this, "댓글을 신고했습니다.", Toast.LENGTH_SHORT).show()
//                // 신고 API
//            }
//
//
//        }
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