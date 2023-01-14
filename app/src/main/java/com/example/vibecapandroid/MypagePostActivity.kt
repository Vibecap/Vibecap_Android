package com.example.vibecapandroid

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MypagePostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_post)


        var editArray: Array<String> = arrayOf("수정하기", "삭제하기", "게시물 공유하기") // 리스트에 들어갈 Array
        val post_menu =findViewById<ImageView>(R.id.activity_mypage_post_menu)
        post_menu.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("컬러 선택하기")
                .setItems(editArray,
                    DialogInterface.OnClickListener { dialog, which ->
                        // 여기서 인자 'which'는 배열의 position 나타냄
                        val postedit_title = findViewById<TextView>(R.id.activity_mypage_postedit_title)
                        postedit_title.text = editArray[which]
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }

    }
}