package com.example.vibecapandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryAlbumActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var arrayList:ArrayList<HistoryAlbumimageClass> ? = null
    private var historyAlbumAdapters:HistoryAlbumadaptersClass ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_album)

        recyclerView = findViewById(R.id.recyclerview)
        gridLayoutManager = GridLayoutManager(applicationContext,3,
            LinearLayoutManager.VERTICAL,false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        arrayList = ArrayList()
        arrayList = setDataInList()
        historyAlbumAdapters = HistoryAlbumadaptersClass(applicationContext,arrayList!!)
        recyclerView?.adapter = historyAlbumAdapters
    }

    private fun setDataInList(): ArrayList<HistoryAlbumimageClass>{
        var items: ArrayList<HistoryAlbumimageClass> = ArrayList()

        items.add(HistoryAlbumimageClass(R.drawable.ic_launcher_background))
        items.add(HistoryAlbumimageClass(R.drawable.ic_launcher_background))
        items.add(HistoryAlbumimageClass(R.drawable.ic_launcher_background))


        return items
    }
}