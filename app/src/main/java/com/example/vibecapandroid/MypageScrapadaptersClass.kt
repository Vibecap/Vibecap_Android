package com.example.vibecapandroid

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.CheckMypageScrapResponseResult

class MypageScrapadaptersClass(var context: Context, var arrayList: ArrayList<CheckMypageScrapResponseResult>):
    RecyclerView.Adapter<MypageScrapadaptersClass.ItemHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.activity_mypage_scrapgrid,parent,false)
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: MypageScrapadaptersClass.ItemHolder, position: Int) {
        var mypageScrapImage: CheckMypageScrapResponseResult = arrayList.get(position)
        holder.apply {
            Glide.with(context).load(mypageScrapImage.vibe_image).into(images)
        }

        holder.itemView.setOnClickListener {
            val intent= Intent(holder.itemView.context,VibePostActivity::class.java)
            intent.putExtra("post_id",arrayList[position].post_id.toInt())
            ContextCompat.startActivity(holder.itemView.context,intent,null)
            Log.d("position","${position}")
            Log.d("post_id","${arrayList[position].post_id}")
        }

    }



    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var images = itemView.findViewById<ImageView>(R.id.item_history_mypagescrap_all_posts_iv)
        init {
            itemView.setOnClickListener {
                Log.d("Click", "Click")
            }
        }
    }

}
/*
class MypageScrapimageClass {

    var image :String

    constructor(image: String) {
        this.image = image
    }
}*/