package com.example.vibecapandroid

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.vibecapandroid.coms.PostWeeklyResponse
import com.example.vibecapandroid.coms.PostweeklyData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ViewPagerAdapter(var context: Context, weeklyList: Array<PostweeklyData>?)  : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    var item = weeklyList

    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.fragment_vibe_main_weekly_item, parent, false)){

        val weekly = itemView.findViewById<ImageView>(R.id.imageView_weekly_item1)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = item!!.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.apply{
            Glide.with(context)
                .load(item?.get(position)?.vibe_image)
                .transform(CenterCrop(), RoundedCorners(20))
                .into(weekly)
        }
        holder.itemView.setOnClickListener {
            VibeMainFragment.WeeklyRetrofitObject.getApiService().postWeeklyCheck()
                .enqueue(object : Callback<PostWeeklyResponse> {
                    override fun onResponse(
                        call: Call<PostWeeklyResponse>,
                        response: Response<PostWeeklyResponse>
                    ) {
                       val responseData = response.body()
                        if(response.isSuccessful){
                            if(responseData != null){
                                var intent = Intent(context,VibePostActivity::class.java)
                                val position = holder.absoluteAdapterPosition
                                intent.putExtra("post_id",responseData.result[position].post_id)
                                context.startActivity(intent)
                            }
                        }
                    }
                    override fun onFailure(call: Call<PostWeeklyResponse>, t: Throwable) {
                        Log.d("weekly Click error","${t.toString()}")
                    }

                })
        }
    }
}