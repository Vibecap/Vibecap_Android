package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MypageLikeActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var arrayList:ArrayList<MypageLikeimageClass> ? = null
    private var mypageLikeAdapters:MypageLikeadaptersClass ? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_like)

        recyclerView = findViewById(R.id.recyclerview)
        gridLayoutManager = GridLayoutManager(applicationContext,3,
            LinearLayoutManager.VERTICAL,false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        arrayList = ArrayList()
        arrayList = setDataInList()
        mypageLikeAdapters = MypageLikeadaptersClass(applicationContext,arrayList!!)
        recyclerView?.adapter = mypageLikeAdapters





        val mypage_back = findViewById<ImageView>(R.id.activity_mypage_like_back)
        mypage_back.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MypageProfileActivity::class.java)
            startActivity(intent)
        })

        //안됨 (하나하나 클릭했을때 그에 따른 이미지가 보이도록 수정)
        val album_list = findViewById<ImageView>(R.id.activity_mypage_album_image)
        album_list.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,MypagePostActivity::class.java)
            startActivity(intent)
        })



    }



    private fun setDataInList(): ArrayList<MypageLikeimageClass>{
        var items: ArrayList<MypageLikeimageClass> = ArrayList()



        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list3))


        return items




        
    }
}