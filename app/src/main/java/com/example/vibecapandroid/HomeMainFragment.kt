package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.example.vibecapandroid.WheelView.WheelView
import com.example.vibecapandroid.WheelView.adapter.WheelAdapter
import com.example.vibecapandroid.databinding.FragmentHomeMainBinding
import java.lang.Math.abs


public lateinit var feeling : String

class HomeMainFragment : Fragment() {
    private var wheelView: WheelView? = null
    private lateinit var viewBinding: FragmentHomeMainBinding
    //size 설정
    var size = 8

//    var sound : Uri =RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//    var ringtone = RingtoneManager.getRingtone(context,sound)


    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentHomeMainBinding.inflate(layoutInflater)


        // 상태바 설정
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
        val windowController = WindowInsetsControllerCompat(requireActivity().window, requireActivity().window.decorView)
        windowController.isAppearanceLightStatusBars = false

        requireActivity().window.apply {
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }



        viewBinding.imageButtonAlarm.setOnClickListener{
            val intent = Intent(context, MypageAlarmActivity::class.java)
            startActivity(intent)
        }
        viewBinding.imageButtonProfile.setOnClickListener{
            val intent = Intent(context, MypageProfileActivity::class.java)
            startActivity(intent)
        }




        val vibrator = requireContext().getSystemService(VIBRATOR_SERVICE) as Vibrator
        val layout: ConstraintLayout = viewBinding.wheelMain
        layout.setBackgroundResource(R.raw.bg_img_sinna)

        
        wheelView = viewBinding.wheelview
        wheelView!!.setWheelItemCount(size)
        val textView = viewBinding.fragmentHomeMainFeeling
        
        textView.text = "신나는"

        val shapeDrawables = arrayOfNulls<ShapeDrawable>(size)
        val colors = arrayOfNulls<String>(size)
        for (i in 0 until size) {
            shapeDrawables[i] = ShapeDrawable(OvalShape())
            colors[i] = "#26000000"
            shapeDrawables[i]!!.paint.color = Color.parseColor(colors[i])
        }


        //wheelView 초기화
        wheelView!!.adapter = object : WheelAdapter {
            override fun getDrawable(position: Int): Drawable {
                var drawable:Drawable = resources.getDrawable(R.drawable.wheel_not_selected)
                return drawable
            }

            override fun getCount(): Int {
                return size
            }
        }
//        wheelView!!.setOnTouchListener { v, event ->
//            var action:Int = event.getAction()
//            if (action == MotionEvent.ACTION_UP) {
//                val intent = Intent(activity, HomeCameraActivity::class.java)
//                startActivity(intent)
//
//            }
//            true
//        }
        //var drawable:Drawable = resources.getDrawable(R.drawable.wheel_main)
//        wheelView!!.setWheelDrawable(drawable)



        wheelView!!.onWheelAngleChangeListener =
            WheelView.OnWheelAngleChangeListener { angle->
                var angler = angle % 360
               // Log.d("angler",angler.toString())
                if((angler>=0 && angler < 40)||(angler>=-360 && angler <-320 )){
                        layout.setBackgroundResource(R.raw.bg_img_sinna)
                        textView.text = "신나는"
                        feeling = textView.text as String
                        wheelView!!.setWheelDrawable(R.drawable.wheel_sinna)


                }
                else if((angler>=40 && angler<80)||(angler>=-320 && angler <-280 )){
                    layout.setBackgroundResource(R.raw.bg_img_pogen)
                    textView.text = "포근한"
                    feeling = textView.text as String
                    wheelView!!.setWheelDrawable(R.drawable.wheel_pogen)

                }
                else if((angler>=80 && angler<120)||(angler>=-280 && angler <-240 )){
                    layout.setBackgroundResource(R.raw.bg_img_nangman)
                    textView.text = "낭만적인"
                    feeling = textView.text as String
                    wheelView!!.setWheelDrawable(R.drawable.wheel_nangman)
                    //Log.d("angle", angle.toString())
                }
                else if((angler>=120 && angler<160)||(angler>=-240 && angler <-200 )){

                    layout.setBackgroundResource(R.raw.bg_img_simsim)
                    textView.text = "심심한"
                    feeling = textView.text as String
                    wheelView!!.setWheelDrawable(R.drawable.wheel_simsim)
                    //Log.d("angle", angle.toString())
                }
                else if((angler>=160 && angler<200)||(angler>=-200 && angler <-160 )){
                    layout.setBackgroundResource(R.raw.bg_img_zanzan)
                    textView.text = "잔잔한"
                    feeling = textView.text as String
                    wheelView!!.setWheelDrawable(R.drawable.wheel_zanzan)

                }
                else if((angler>=200 && angler<240)||(angler>=-160 && angler <-120 )){
                    layout.setBackgroundResource(R.raw.bg_img_woowool)
                    textView.text = "우울한"
                    feeling = textView.text as String
                    wheelView!!.setWheelDrawable(R.drawable.wheel_woowool)
                }
                else if((angler>=240 && angler<280)||(angler>=-120  && angler <-80 )){
                    layout.setBackgroundResource(R.raw.bg_img_gonghe)
                    textView.text = "공허한"
                    feeling = textView.text as String
                    wheelView!!.setWheelDrawable(R.drawable.wheel_gonghe)
                }
                else if((angler>=280 && angler<320)||(angler>=-80 && angler <-40 )){
                    layout.setBackgroundResource(R.raw.bg_img_sunsun)
                    textView.text = "선선한"
                    feeling = textView.text as String
                    wheelView!!.setWheelDrawable(R.drawable.wheel_sunsun)
                }
            }

       // wheelView!!.setOnWheelAngleChangeListener (wheelView!!.onWheelAngleChangeListener)


        wheelView!!.setOnWheelItemSelectedListener { parent, itemDrawable, position ->
            if (position!=null) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(50, 10))


                }
            }
        }
        return viewBinding.root!!
        /* return view*/

    }

}


