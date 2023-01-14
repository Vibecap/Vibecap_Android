package com.example.vibecapandroid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class MypageLikeadaptersClass(var context: Context, var arrayList: ArrayList<MypageLikeimageClass>):
    RecyclerView.Adapter<MypageLikeadaptersClass.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_mypage_likegrid, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var mypageLikeImage: MypageLikeimageClass = arrayList.get(position)
        holder.images.setImageResource(mypageLikeImage.image!!)


    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var images = itemView.findViewById<ImageView>(R.id.image)
    }


}



class MypageLikeimageClass {

    var image :Int ? = 0

    constructor(image: Int?) {
        this.image = image
    }
}