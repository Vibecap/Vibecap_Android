package com.example.vibecapandroid

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var historyMainImage:HistoryMainImageClass = arrayList.get(position)
        holder.images.setImageBitmap((historyMainImage.image!!))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var images: ImageView = itemView.findViewById<ImageView>(R.id.image)
    }
}
class HistoryMainImageClass {
    var image :Bitmap
    constructor(image: Bitmap) {
        this.image = image
    }
}