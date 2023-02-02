package com.example.vibecapandroid

import android.annotation.SuppressLint
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
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.vibecapandroid.coms.CheckMypageLikeResponseResult



class MypageLikeadaptersClass(var context: Context, var arrayList: ArrayList<CheckMypageLikeResponseResult>):
    RecyclerView.Adapter<MypageLikeadaptersClass.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.activity_mypage_likegrid, parent, false)
        return ItemHolder(itemHolder)
    }




    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var mypageLikeImage: CheckMypageLikeResponseResult = arrayList.get(position)
        holder.apply {
            if(mypageLikeImage.vibe_image!=null&&images!=null)
            Glide.with(context).load(mypageLikeImage.vibe_image).into(images)
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

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var images = itemView.findViewById<ImageView>(R.id.item_history_mypagelike_all_posts_iv)
        init {
            itemView.setOnClickListener {
                Log.d("Click", "Click")
            }
        }
    }


}

/*

class MypageLikeimageClass {

    var image : String

    constructor(image: String) {
        this.image = image
    }
}*/