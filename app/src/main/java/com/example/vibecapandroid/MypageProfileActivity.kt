package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MypageProfileActivity : AppCompatActivity() {

    var mypageList = arrayListOf<MypageProfileClass>(
        MypageProfileClass(R.drawable.ic_activity_mypage_profile_act,"활동 내역",R.drawable.ic_activity_mypage_profile_next),
        MypageProfileClass(R.drawable.ic_activity_mypage_profile_writed,"내 게시물",R.drawable.ic_activity_mypage_profile_next),
        MypageProfileClass(R.drawable.ic_activity_mypage_profile_like,"좋아요 한 게시물",R.drawable.ic_activity_mypage_profile_next),
        MypageProfileClass(R.drawable.ic_activity_mypage_profile_scrap,"스크랩 한 게시물",R.drawable.ic_activity_mypage_profile_next),
        MypageProfileClass(R.drawable.ic_activity_mypage_profile_setting,"설정",R.drawable.ic_activity_mypage_profile_next)

    )


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_profilelist)

        val Adapter = MypageProfileadaptersClass(this,mypageList)
        val activity_mypage_profilelist = findViewById<ListView>(R.id.activity_mypage_profilelist)
        activity_mypage_profilelist.adapter = Adapter

        activity_mypage_profilelist.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if(position==0){
                val selectItem = parent.getItemAtPosition(position) as MypageProfileClass
                val intent = Intent(this,MypageAlarmActivity::class.java)
                intent.putExtra("활동내역",mypageList[position].profile_textview)
                startActivity(intent)}
            else if(position==1){
                val selectItem = parent.getItemAtPosition(position) as MypageProfileClass
                val intent = Intent(this,MypageWritedActivity::class.java)
                intent.putExtra("내 게시물",mypageList[position].profile_textview)
                startActivity(intent)}
            else if(position==2){
                val selectItem = parent.getItemAtPosition(position) as MypageProfileClass
                val intent = Intent(this,MypageLikeActivity::class.java)
                intent.putExtra("좋아요 한 게시물",mypageList[position].profile_textview)
                startActivity(intent)}
            else if(position==3){
                val selectItem = parent.getItemAtPosition(position) as MypageProfileClass
                val intent = Intent(this,MypageScrapActivity::class.java)
                intent.putExtra("스크랩 한 게시물",mypageList[position].profile_textview)
                startActivity(intent)}
            else if(position==4){
                val selectItem = parent.getItemAtPosition(position) as MypageProfileClass
                val intent = Intent(this,MypageSetupActivity::class.java)
                intent.putExtra("설정",mypageList[position].profile_textview)
                startActivity(intent)}
        }

        val mypage_back = findViewById<ImageView>(R.id.activity_mypage_nickname_close)
        mypage_back.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })


    }





        }




