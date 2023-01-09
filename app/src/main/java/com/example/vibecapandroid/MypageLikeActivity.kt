package com.example.vibecapandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MypageLikeActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var arrayList:ArrayList<MypageLikeimageClass> ? = null
    private var mypageLikeAdapters:MypageLikeadaptersClass ? = null

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
    }

    private fun setDataInList(): ArrayList<MypageLikeimageClass>{
        var items: ArrayList<MypageLikeimageClass> = ArrayList()

        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageLikeimageClass(R.drawable.image_ic_activity_history_album_list3))




        return items
    }
}