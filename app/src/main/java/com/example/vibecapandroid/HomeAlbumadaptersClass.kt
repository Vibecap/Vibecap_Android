package com.example.vibecapandroid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class HomeAlbumadaptersClass(var context: Context, var arrayList: ArrayList<HomeAlbumimageClass>):
    RecyclerView.Adapter<HomeAlbumadaptersClass.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.activity_home_albumgrid,parent,false)
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var homeAlbumImage: HomeAlbumimageClass = arrayList.get(position)
        holder.images.setImageResource(homeAlbumImage.image!!)

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var images = itemView.findViewById<ImageView>(R.id.image)
    }
}

class HomeAlbumimageClass {

    var image :Int ? = 0

    constructor(image: Int?) {
        this.image = image
    }
}