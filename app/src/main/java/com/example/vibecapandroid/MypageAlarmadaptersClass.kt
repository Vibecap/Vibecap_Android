package com.example.vibecapandroid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MypageAlarmadaptersClass(var alarmList: ArrayList<MypageAlarmClass>):
    RecyclerView.Adapter<MypageAlarmadaptersClass.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MypageAlarmadaptersClass.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_mypage_alarmlist,parent,false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.image.setImageResource(alarmList.get(position).image)
        holder.title.text=alarmList.get(position).title
        holder.id.text= alarmList.get(position).id
        holder.text.text=alarmList.get(position).text
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image = itemView.findViewById<ImageView>(R.id.activity_mypage_alarm_imageview)
        var title = itemView.findViewById<TextView>(R.id.activity_mypage_alarm_title)
        var id = itemView.findViewById<TextView>(R.id.activity_mypage_alarm_icon)
        var text = itemView.findViewById<TextView>(R.id.activity_mypage_alarm_text)
    }
}

class MypageAlarmClass (val image: Int, val title: String, val id: String, val text:String)
