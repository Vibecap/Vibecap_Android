package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class VibePostActivity : AppCompatActivity() {

    private var article_popup:ImageButton? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vibe_post)




        // 게시글 popup
        val article_popup = findViewById<ImageButton>(R.id.article_popup)
        article_popup.setOnClickListener {
            // Dialog만들기
            val mDialogView =
                LayoutInflater.from(this).inflate(R.layout.activity_popup, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            val mAlertDialog = mBuilder.show()

            val block_table = mDialogView.findViewById<Button>(R.id.dialog_table_block)
            block_table.setOnClickListener {

                Toast.makeText(this, "게시물을 차단했습니다.", Toast.LENGTH_SHORT).show()
                // 차단 API
            }

            val declaration_table = mDialogView.findViewById<Button>(R.id.dialog_table_declaration)
            declaration_table.setOnClickListener {

                var intent = Intent(this, PopupActivity::class.java)
                startActivity(intent)
            }

            val copy_table = mDialogView.findViewById<Button>(R.id.dialog_table_copy)
            copy_table.setOnClickListener {

                Toast.makeText(this, "링크를 복사했습니다.", Toast.LENGTH_SHORT).show()
                // 링크 복사 API
            }

            val share_table = mDialogView.findViewById<Button>(R.id.dialog_table_share)
            share_table.setOnClickListener {

                Toast.makeText(this, "토스트 메시지", Toast.LENGTH_SHORT).show()
                // 게시물 공유 API
            }
        }

        // 댓글 popup
        val comment_popup = findViewById<ImageButton>(R.id.imageButton_popup)
        comment_popup.setOnClickListener {
            // Dialog만들기
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.activity_popup_comment, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            val mAlertDialog = mBuilder.show()

            val block_table = mDialogView.findViewById<Button>(R.id.block_table)
            block_table.setOnClickListener {

                Toast.makeText(this, "댓글 작성자를 차단했습니다.", Toast.LENGTH_SHORT).show()
                // 차단 API
            }

            val declaration_table = mDialogView.findViewById<Button>(R.id.declaration_table)
            declaration_table.setOnClickListener {

                Toast.makeText(this, "댓글을 신고했습니다.", Toast.LENGTH_SHORT).show()
                // 신고 API
            }


        }

    }
}