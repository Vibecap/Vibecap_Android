package com.example.vibecapandroid

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.HistoryApiInterface
import com.example.vibecapandroid.coms.HistoryMainImageClass
import com.example.vibecapandroid.coms.HistoryOneResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HistoryMainAdaptersClass(var context: Context, var arrayList: ArrayList<HistoryMainImageClass>): RecyclerView.Adapter<HistoryMainAdaptersClass.ItemHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_history_maingrid, parent, false)
        return ItemHolder(itemHolder)
    }


    override fun onBindViewHolder(holder: ItemHolder, position:Int) {
        var historyMainImage: HistoryMainImageClass = arrayList.get(position)
        //holder.images?.setImageURI((historyMainImage.image.toUri()))
        val apiService=retrofit.create(HistoryApiInterface::class.java)

        holder.apply{
            Glide.with(context).load(historyMainImage.vibe_image).into(images)
        }

        holder.itemView.setOnClickListener {
            apiService.getHistoryOne(userToken, arrayList.get(position).vibe_id)
                .enqueue(object : Callback<HistoryOneResponse> {
                    override fun onResponse(call: Call<HistoryOneResponse>, response: Response<HistoryOneResponse>) {
                        val responseData=response.body()
                        if(response.isSuccessful){
                            if (responseData != null) {
                                if(responseData.is_success) {
                                    //arrayList?.add(HistoryMainImageClass((responseData.result.album[0].vibe_image)))
                                    val intent = Intent(it.context, HistoryYoutubeActivity::class.java)
                                    val position=holder.absoluteAdapterPosition.toInt()
                                    intent.putExtra("position",position)
                                    Log.d("sendposition","${position}")
                                    intent.putExtra("video_id",responseData.result.youtube_link)
                                    intent.putExtra("vibe_id",responseData.result.vibe_id.toInt())
                                    intent.putExtra("vibe_keywords",responseData.result.vibe_keywords.toString())
                                    intent.putExtra("vibe_image",responseData.result.vibe_image.toString())
                                    Log.d("adapter keywords","${responseData.result.vibe_keywords.toString()}")
                                    it.context.startActivity(intent)
                                }
                                else{Log.d("getHistoryOne 통신 Fail","Fail Data is null")}
                            } else { Log.d("getHistoryOne","getHistoryOne Response Null data") }
                        } else{ Log.d("getHistoryOne","getHistoryOne Response Response Not Success") }
                    } override fun onFailure(call: Call<HistoryOneResponse>, t: Throwable) { Log.d("getHistoryOne","${t.toString()}")}
                })
        }
    }
    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var images: ImageView = itemView.findViewById(R.id.item_history_history_all_posts_iv)
        init {
            itemView.setOnClickListener {
                Log.d("Click", "Click")
            }
        }
    }
}
