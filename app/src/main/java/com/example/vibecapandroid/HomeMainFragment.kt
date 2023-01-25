package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.vibecapandroid.databinding.FragmentHomeMainBinding
import com.lukedeighton.wheelview.WheelView
import com.lukedeighton.wheelview.WheelView.OnWheelItemClickListener
import com.lukedeighton.wheelview.adapter.WheelAdapter



class HomeMainFragment : Fragment() {
    private var wheelView: WheelView? = null
    private lateinit var viewBinding: FragmentHomeMainBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
   private val binding get() = viewBinding!!
    //위에 코드는 아마 다른 프래그먼트에서 다시 돌아왓을떄 해당 프래그먼트를 다시 살릴지 여부를 결정하는 코드인듯

    //size 설정
    var size = 7

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentHomeMainBinding.inflate(layoutInflater)
        val view = inflater.inflate(R.layout.fragment_home_main, container, false)

        val search: ImageButton = view.findViewById(R.id.main_alarm)
        search.setOnClickListener {
            val intent = Intent(context, MypageAlarmActivity::class.java)
            startActivity(intent)
        }

        val addpost: ImageButton = view.findViewById(R.id.main_profile)
        addpost.setOnClickListener {
            val intent = Intent(context, MypageProfileActivity::class.java)
            startActivity(intent)
        }


        val layout: ConstraintLayout = viewBinding.wheelMain as ConstraintLayout
        wheelView = viewBinding.wheelview as WheelView
        wheelView!!.setWheelItemCount(size)
        val textView = viewBinding.textView2 as TextView
        val shapeDrawables = arrayOfNulls<ShapeDrawable>(size)
        val colors = arrayOfNulls<String>(size)
        for (i in 0 until size) {
            shapeDrawables[i] = ShapeDrawable(OvalShape())
            colors[i] = "#26000000"
            shapeDrawables[i]!!.paint.color = Color.parseColor(colors[i])
        }
        wheelView!!.adapter = object : WheelAdapter {
            override fun getDrawable(position: Int): Drawable {
                return shapeDrawables[position]!!
            }

            override fun getCount(): Int {
                return size
            }
        }

        wheelView!!.onWheelItemClickListener =
            OnWheelItemClickListener { parent, position, isSelected ->
                when (position) {
                    0 -> {
                        Log.d("Tag", "Clicked")
                    }
                }
                //the position in the adapter and whether it is closest to the selection angle

            }

        wheelView!!.setOnWheelItemSelectedListener { parent, itemDrawable, position ->
            when (position) {
                0 -> {
                    layout.setBackgroundResource(R.raw.bg_img_pogen)
                    textView.text = "포근한"

                }
                1 -> {
                    layout.setBackgroundResource(R.raw.bg_img_gonghe)
                    textView.text = "공허한"
                }
                2 -> {
                    layout.setBackgroundResource(R.raw.bg_img_nangman)
                    textView.text = "낭만적인"
                }
                3 -> {
                    layout.setBackgroundResource(R.raw.bg_img_sinna)
                    textView.text = "신나는"
                }
                4 -> {
                    layout.setBackgroundResource(R.raw.bg_img_zanzan)
                    textView.text = "잔잔한"
                }
                5 -> {
                    layout.setBackgroundResource(R.raw.bg_img_woowool)
                    textView.text = "우울한"
                }
                6 -> {
                    layout.setBackgroundResource(R.raw.bg_img_sunsun)
                    textView.text = "선선한"
                }
            }
        }
        return view

    }
}

