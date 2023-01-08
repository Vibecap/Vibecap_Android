package com.example.vibecapandroid

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class HistoryMainAdaptersClass(var context: Context, var arrayList: ArrayList<HistoryMainImageClass>):
    RecyclerView.Adapter<HistoryMainAdaptersClass.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.fragment_history_maingrid,parent,false)
        Log.d(TAG,"OnAdapterCreated")
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var historyMainImage:HistoryMainImageClass = arrayList.get(position)
        holder.images.setImageResource(historyMainImage.image!!)

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var images = itemView.findViewById<ImageView>(R.id.image)
    }
}

class HistoryMainImageClass {

    var image :Int ? = 0

    constructor(image: Int?) {
        this.image = image
    }
}