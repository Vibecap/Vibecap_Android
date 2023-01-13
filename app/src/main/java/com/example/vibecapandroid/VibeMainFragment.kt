package com.example.vibecapandroid


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.vibecapandroid.databinding.FragmentVibeMainBinding

class VibeMainFragment : Fragment() {
    private lateinit var viewBinding: FragmentVibeMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding=FragmentVibeMainBinding.inflate(layoutInflater)



        return viewBinding.root
        //return inflater.inflate(R.layout.fragment_vibe_main, container, false)
        //R.id 방식 하실꺼면 바로 윗줄 꺼 쓰시면 됩니다~!~!. return값으로요
    }



}