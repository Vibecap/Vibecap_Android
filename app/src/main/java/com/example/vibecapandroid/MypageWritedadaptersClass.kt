package com.example.vibecapandroid

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.CheckMypageWritedResponseResult

class MypageWritedadaptersClass(var context: Context, var arrayList: ArrayList<CheckMypageWritedResponseResult>):
    RecyclerView.Adapter<MypageWritedadaptersClass.ItemHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.activity_mypage_writedgrid,parent,false)
        return ItemHolder(itemHolder)
    }


    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var mypageWritedImage: CheckMypageWritedResponseResult = arrayList.get(position)
        holder.apply {
            Glide.with(context).load(mypageWritedImage.vibe_image).into(images)
        }

        holder.itemView.setOnClickListener {
            val intent= Intent(holder.itemView.context,MypagePostActivity::class.java)

            intent.putExtra("post_id",arrayList[position].post_id.toInt())
            intent.putExtra("vibe_id",arrayList[position].vibe_id.toInt())
            ContextCompat.startActivity(holder.itemView.context,intent,null)
            Log.d("position","${position}")
            Log.d("post_id","${arrayList[position].post_id}")

        }



    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var images = itemView.findViewById<ImageView>(R.id.item_history_mypagewrited_all_posts_iv)
        init {
            itemView.setOnClickListener {
                Log.d("Click", "Click")
            }
        }

    }
}
/*
class MypageWritedimageClass {

    var image :String

    constructor(image: String) {
        this.image = image
    }

}*/