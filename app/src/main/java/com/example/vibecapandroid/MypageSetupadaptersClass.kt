package com.example.vibecapandroid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView


class MypageSetupClass (val profile_imageview: Int, val profile_textview: String, val profile_imagenext: Int)
class MypageSetupadaptersClass(val context: Context, val setupList:ArrayList<MypageSetupClass>): BaseAdapter()
{
    override fun getCount(): Int {
    return setupList.size

}

    override fun getItem(position: Int): Any {
        return setupList[position]

    }

    override fun getItemId(position: Int): Long {
        return 0

    }

    override fun getView(position: Int,convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.activity_mypage_setup,null)

        val profile_imageview= view.findViewById<ImageView>(R.id.activity_mypage_setup_imageview)
        val profile_textview = view.findViewById<TextView>(R.id.activity_mypage_setup_textview)
        val profile_imagenext= view.findViewById<ImageView>(R.id.activity_mypage_setup_next)

        val mypage = setupList[position]
        profile_imageview.setImageResource(mypage.profile_imageview)
        profile_textview.text=mypage.profile_textview
        profile_imagenext.setImageResource(mypage.profile_imagenext)

        return view
    }

}