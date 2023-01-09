package com.example.vibecapandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeAlbumActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var arrayList:ArrayList<HomeAlbumimageClass> ? = null
    private var homeAlbumAdapters:HomeAlbumadaptersClass ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_album)

        recyclerView = findViewById(R.id.recyclerview)
        gridLayoutManager = GridLayoutManager(applicationContext,3,
            LinearLayoutManager.VERTICAL,false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        arrayList = ArrayList()
        arrayList = setDataInList()
        homeAlbumAdapters = HomeAlbumadaptersClass(applicationContext,arrayList!!)
        recyclerView?.adapter = homeAlbumAdapters
    }

    private fun setDataInList(): ArrayList<HomeAlbumimageClass>{
        var items: ArrayList<HomeAlbumimageClass> = ArrayList()

        items.add(HomeAlbumimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(HomeAlbumimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(HomeAlbumimageClass(R.drawable.image_ic_activity_history_album_list3))




        return items
    }
}