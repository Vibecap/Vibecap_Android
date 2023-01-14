package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MypageWritedActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var arrayList:ArrayList<MypageWritedimageClass> ? = null
    private var mypageWritedAdapters:MypageWritedadaptersClass ? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_writed)

        recyclerView = findViewById(R.id.recyclerview)
        gridLayoutManager = GridLayoutManager(applicationContext,3,
            LinearLayoutManager.VERTICAL,false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        arrayList = ArrayList()
        arrayList = setDataInList()
        mypageWritedAdapters = MypageWritedadaptersClass(applicationContext,arrayList!!)
        recyclerView?.adapter = mypageWritedAdapters


        val mypage_back = findViewById<ImageView>(R.id.activity_mypage_writed_back)
        mypage_back.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MypageProfileActivity::class.java)
            startActivity(intent)
        })

    }

    private fun setDataInList(): ArrayList<MypageWritedimageClass>{
        var items: ArrayList<MypageWritedimageClass> = ArrayList()

        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list3))




        return items
    }
}