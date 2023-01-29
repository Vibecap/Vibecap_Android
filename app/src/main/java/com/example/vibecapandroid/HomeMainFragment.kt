package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.vibecapandroid.WheelView.WheelView
import com.example.vibecapandroid.WheelView.adapter.WheelAdapter
import com.example.vibecapandroid.databinding.FragmentHomeMainBinding
import java.lang.Math.abs



public lateinit var feeling : String

class HomeMainFragment : Fragment() {
    private var wheelView: WheelView? = null
    private lateinit var viewBinding: FragmentHomeMainBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
    //private val binding get() = viewBinding!!
    //위에 코드는 아마 다른 프래그먼트에서 다시 돌아왓을떄 해당 프래그먼트를 다시 살릴지 여부를 결정하는 코드인듯

    //size 설정
    var size = 8



    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentHomeMainBinding.inflate(layoutInflater)

        viewBinding.mainAlarm.setOnClickListener{
            val intent = Intent(context, MypageAlarmActivity::class.java)
            startActivity(intent)
        }
        viewBinding.mainProfile.setOnClickListener{
            val intent = Intent(context, MypageProfileActivity::class.java)
            startActivity(intent)
        }

        val vibrator = requireContext().getSystemService(VIBRATOR_SERVICE) as Vibrator
        val layout: ConstraintLayout = viewBinding.wheelMain
        wheelView = viewBinding.wheelview
        wheelView!!.setWheelItemCount(size)
        val textView = viewBinding.fragmentHomeMainFeeling
        val shapeDrawables = arrayOfNulls<ShapeDrawable>(size)
        val colors = arrayOfNulls<String>(size)
        for (i in 0 until size) {
            shapeDrawables[i] = ShapeDrawable(OvalShape())
            colors[i] = "#26000000"
            shapeDrawables[i]!!.paint.color = Color.parseColor(colors[i])
        }

        wheelView!!.setWheelDrawableRotatable(false)


        //wheelView 초기화
        wheelView!!.adapter = object : WheelAdapter {
            override fun getDrawable(position: Int): Drawable {
                return shapeDrawables[position]!!
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


//        var drawable:Drawable = resources.getDrawable(R.drawable.ic_wheel_main)
//        wheelView!!.setWheelDrawable(drawable)



        viewBinding.HomeMainWheelButton.setOnClickListener{
            //button 없애기
            viewBinding.HomeMainWheelButton.visibility =GONE
            //흰색 글자 만들어주기
            context?.let { it1 -> viewBinding.fragmentHomeMainTitle.setTextColor(it1.getColor(R.color.white)) }

        }

        wheelView!!.onWheelAngleChangeListener =
            WheelView.OnWheelAngleChangeListener { angle->
                var angler = abs(angle % 360)
                //Log.d("angler", angler.toString())
                if(angler>=0){
                    layout.setBackgroundResource(R.raw.bg_img_pogen)
                    textView.visibility=VISIBLE
                    textView.text = "포근한"
                    feeling = textView.text as String

                }
                if(angler>=45){
                    layout.setBackgroundResource(R.raw.bg_img_gonghe)
                    textView.visibility=VISIBLE
                    textView.text = "공허한"
                    feeling = textView.text as String

                }
                if(angler>=90){
                    layout.setBackgroundResource(R.raw.bg_img_nangman)
                    textView.visibility=VISIBLE
                    textView.text = "낭만적인"
                    feeling = textView.text as String
                    //Log.d("angle", angle.toString())
                }
                if(angler>=135){
                    layout.setBackgroundResource(R.raw.bg_img_sinna)
                    textView.visibility=VISIBLE
                    textView.text = "신나는"
                    feeling = textView.text as String
                    //Log.d("angle", angle.toString())
                }
                if(angler>=180){
                    layout.setBackgroundResource(R.raw.bg_img_zanzan)
                    textView.visibility=VISIBLE
                    textView.text = "잔잔한"
                    feeling = textView.text as String

                }
                if(angler>=225){
                    layout.setBackgroundResource(R.raw.bg_img_woowool)
                    textView.visibility=VISIBLE
                    textView.text = "우울한"
                    feeling = textView.text as String
                }
                if(angler>=270){
                    layout.setBackgroundResource(R.raw.bg_img_gonghe)
                    textView.visibility=VISIBLE
                    textView.text = "공허한"
                    feeling = textView.text as String
                }
                if(angler>=315){
                    layout.setBackgroundResource(R.raw.bg_img_gonghe)
                    textView.visibility=VISIBLE
                    textView.text = "공허한"
                    feeling = textView.text as String
                }
            }

        wheelView!!.setOnWheelAngleChangeListener (wheelView!!.onWheelAngleChangeListener)


        wheelView!!.setOnWheelItemSelectedListener { parent, itemDrawable, position ->
            if (position!=null) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, 100))


                }
            }
        }
        return viewBinding.root!!
        /* return view*/

    }


}


