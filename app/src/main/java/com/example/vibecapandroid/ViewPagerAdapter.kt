package com.example.vibecapandroid

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.PostweeklyData


class ViewPagerAdapter(var context: Context, weeklyList: Array<PostweeklyData>?) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    var item = weeklyList

    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.fragment_vibe_main_weekly_item, parent, false)){

        val weekly = itemView.findViewById<ImageView>(R.id.imageView_weekly_item1)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = item!!.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.apply{
            Glide.with(context).load(item?.get(position)?.vibe_image).into(weekly)
        }

    }



}