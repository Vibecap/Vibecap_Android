package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vibecapandroid.MypageAlarmClass
import com.example.vibecapandroid.MypageAlarmadaptersClass
import com.example.vibecapandroid.MypageProfileActivity
import com.example.vibecapandroid.R

class MypageAlarmActivity : AppCompatActivity() {



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_alarm)

        val mypage_back = findViewById<ImageView>(R.id.activity_mypage_alarm_back)
        mypage_back.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MypageProfileActivity::class.java)
            startActivity(intent)
        })

        val alarmList = arrayListOf(
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_like,"내가 작성한 게시물을 좋아요 했습니다","user123","님이 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user123","노래가.좋네요.우리.임영웅..."),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user456","노래 분위기가 너무 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_like,"내가 작성한 게시물을 좋아요 했습니다","user123","님이 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user123","노래가.좋네요.우리.임영웅..."),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user456","노래 분위기가 너무 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_like,"내가 작성한 게시물을 좋아요 했습니다","user123","님이 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user123","노래가.좋네요.우리.임영웅..."),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user456","노래 분위기가 너무 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_like,"내가 작성한 게시물을 좋아요 했습니다","user123","님이 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user123","노래가.좋네요.우리.임영웅..."),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user456","노래 분위기가 너무 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_like,"내가 작성한 게시물을 좋아요 했습니다","user123","님이 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user123","노래가.좋네요.우리.임영웅..."),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user456","노래 분위기가 너무 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_like,"내가 작성한 게시물을 좋아요 했습니다","user123","님이 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user123","노래가.좋네요.우리.임영웅..."),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user456","노래 분위기가 너무 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_like,"내가 작성한 게시물을 좋아요 했습니다","user123","님이 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user123","노래가.좋네요.우리.임영웅..."),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user456","노래 분위기가 너무 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_like,"내가 작성한 게시물을 좋아요 했습니다","user123","님이 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user123","노래가.좋네요.우리.임영웅..."),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user456","노래 분위기가 너무 좋아요")

        )

        val activity_mypage_alarm_recyclerview = findViewById<RecyclerView>(R.id.activity_mypage_alarm_recyclerview)
        activity_mypage_alarm_recyclerview.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        activity_mypage_alarm_recyclerview.setHasFixedSize(true)

        activity_mypage_alarm_recyclerview.adapter = MypageAlarmadaptersClass(alarmList)
    }

    }
