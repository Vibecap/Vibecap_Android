package com.example.vibecapandroid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class MypageProfileClass (val profile_imageview: Int, val profile_textview: String, val profile_imagenext: Int)

class MypageProfileadaptersClass(val context:Context, val mypageList: ArrayList<MypageProfileClass>):BaseAdapter()

{
    override fun getCount(): Int {
        return mypageList.size

    }

    override fun getItem(position: Int): Any {
        return mypageList[position]

    }

    override fun getItemId(position: Int): Long {
        return 0

    }

    override fun getView(position: Int,convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.activity_mypage_profile,null)

        val profile_imageview= view.findViewById<ImageView>(R.id.activity_mypage_alarm_imageview)
        val profile_textview = view.findViewById<TextView>(R.id.activity_mypage_alarm_title)
        val profile_imagenext= view.findViewById<ImageView>(R.id.activity_mypage_profile_next)

        val mypage = mypageList[position]
        profile_imageview.setImageResource(mypage.profile_imageview)
        profile_textview.text=mypage.profile_textview
        profile_imagenext.setImageResource(mypage.profile_imagenext)

        return view
    }

}