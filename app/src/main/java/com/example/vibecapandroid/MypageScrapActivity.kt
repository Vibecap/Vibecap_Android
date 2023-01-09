package com.example.vibecapandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MypageScrapActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var arrayList:ArrayList<MypageScrapimageClass> ? = null
    private var mypageScrapAdapters:MypageScrapadaptersClass ? = null

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
        mypageScrapAdapters = MypageScrapadaptersClass(applicationContext,arrayList!!)
        recyclerView?.adapter = mypageScrapAdapters
    }

    private fun setDataInList(): ArrayList<MypageScrapimageClass>{
        var items: ArrayList<MypageScrapimageClass> = ArrayList()

        items.add(MypageScrapimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageScrapimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageScrapimageClass(R.drawable.image_ic_activity_history_album_list3))




        return items
    }
}