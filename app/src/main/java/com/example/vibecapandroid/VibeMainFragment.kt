package com.example.vibecapandroid


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.vibecapandroid.databinding.FragmentVibeMainBinding

class VibeMainFragment : Fragment() {
    private lateinit var viewBinding: FragmentVibeMainBinding



    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?



    ): View? {
        viewBinding=FragmentVibeMainBinding.inflate(layoutInflater)
        val view = inflater.inflate(R.layout.fragment_vibe_main, container, false)
        //return viewBinding.root
        //R.id 방식 하실꺼면 바로 윗줄 꺼 쓰시면 됩니다~!~!. return값으로요

        val search :ImageButton = view.findViewById(R.id.imageButton_search)
        search.setOnClickListener {
            val intent = Intent(context, SearchActivity::class.java)
            startActivity(intent)
        }

        val addpost : Button = view.findViewById(R.id.btn_addpost)
        addpost.setOnClickListener {
            val intent = Intent(context, HistoryPostActivity::class.java)
            startActivity(intent)
        }

        return view
    }

}