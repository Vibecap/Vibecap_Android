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


    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var historyMainImage: HistoryMainImageClass = arrayList.get(position)
        //holder.images?.setImageURI((historyMainImage.image.toUri()))
        val apiService=retrofit.create(HistoryApiInterface::class.java)

        holder.apply{
            Glide.with(context).load(historyMainImage.vibe_image).into(images)
        }

        holder.itemView.setOnClickListener {

           // val intent=Intent(holder.itemView.context,HomeCapturedActivity::class.java)
         //   ContextCompat.startActivity(holder.itemView.context,intent,null)
            Log.d("찍은 사진 position","${position}")
            apiService.getHistoryOne(userToken, arrayList.get(0).vibe_id)
                .enqueue(object : Callback<HistoryOneResponse> {
                    override fun onResponse(call: Call<HistoryOneResponse>, response: Response<HistoryOneResponse>) {
                        val responseData=response.body()
                        if(response.isSuccessful){
                            if (responseData != null) {
                                Log.d(
                                    "getHistoryOneResponse",
                                    "getHistoryOneResponse\n"+
                                            "isSuccess:${responseData.is_success}\n " +
                                            "Code: ${responseData.code} \n" +
                                            "Message:${responseData.message} \n" +
                                            "Result:${responseData.result.vibe_id}"+
                                            "Result:${responseData.result.member_id}"+
                                            "Result:${responseData.result.vibe_image}"+
                                            "Result:${responseData.result.youtube_link}"+
                                            "Result:${responseData.result.vibe_keywords}"
                                )
                                if(responseData.is_success) {
                                  //arrayList?.add(HistoryMainImageClass((responseData.result.album[0].vibe_image)))
                                    val intent = Intent(it.context, HistoryYoutubeActivity::class.java)
                                    intent.putExtra("video_id",responseData.result.youtube_link)
                                    it.context.startActivity(intent)
                                    Log.d("getHistoryOne 통신 success","success")
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
        var images: ImageView = itemView.findViewById(R.id.image)
        init {
            itemView.setOnClickListener {
                Log.d("Click", "Click")
            }
        }
    }
}
