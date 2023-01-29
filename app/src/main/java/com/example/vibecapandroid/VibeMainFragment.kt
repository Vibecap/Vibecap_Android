package com.example.vibecapandroid


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.vibecapandroid.databinding.FragmentVibeMainBinding

class VibeMainFragment : Fragment() {
    private lateinit var viewBinding: FragmentVibeMainBinding
    private var viewPager: ViewPager2? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        viewBinding = FragmentVibeMainBinding.inflate(layoutInflater)
        val view = inflater.inflate(R.layout.fragment_vibe_main, container, false)

        //  post 테스트 용************
        val testPost :ImageView = view.findViewById(R.id.imageButton_1)
        testPost.setOnClickListener{
            val intent = Intent(context, VibePostActivity::class.java)
            startActivity(intent)
        }
        //**********

        val search :ImageButton = view.findViewById(R.id.imageButton_search)
        search.setOnClickListener {
            val intent = Intent(context, VibeSearchActivity::class.java)
            startActivity(intent)
        }

        val addpost : Button = view.findViewById(R.id.btn_addpost)
        addpost.setOnClickListener {
            val intent = Intent(context, HistoryPostActivity::class.java)
            startActivity(intent)
        }

        val mypage_alarm2: ImageButton = view.findViewById(R.id.imageButton_alarm)
        mypage_alarm2.setOnClickListener {
            val intent = Intent(context, MypageAlarmActivity::class.java)
            startActivity(intent)
        }

        val mypage_profile2:ImageButton = view.findViewById(R.id.imageButton_profile)
        mypage_profile2.setOnClickListener {
            val intent = Intent(context, MypageProfileActivity::class.java)
            startActivity(intent)
        }

        val addview :Button = view.findViewById(R.id.btn_addview)
        addview.setOnClickListener{
            val intent = Intent(context, VibeDetailActivity::class.java)
            startActivity(intent)
        }

        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val pagerAdapter = VibeWeeklyAdapter(requireActivity())

        viewPager?.adapter = pagerAdapter

    }


}