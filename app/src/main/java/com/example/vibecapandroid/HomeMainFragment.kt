package com.example.vibecapandroid

import androidx.appcompat.app.AppCompatActivity
import com.lukedeighton.wheelview.WheelView
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.TextView
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import com.lukedeighton.wheelview.adapter.WheelAdapter
import android.graphics.drawable.Drawable
import com.lukedeighton.wheelview.WheelView.OnWheelItemClickListener
import android.widget.Toast
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.vibecapandroid.databinding.FragmentHomeMainBinding


class HomeMainFragment : Fragment() {
    private var wheelView: WheelView? = null
    private lateinit var viewBinding: FragmentHomeMainBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
   private val binding get() = viewBinding!!
    //위에 코드는 아마 다른 프래그먼트에서 다시 돌아왓을떄 해당 프래그먼트를 다시 살릴지 여부를 결정하는 코드인듯

    //size 설정
    var size = 7
    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding= FragmentHomeMainBinding.inflate(layoutInflater)

        val layout: ConstraintLayout = viewBinding.wheelMain as ConstraintLayout
        wheelView =  viewBinding.wheelview as WheelView
        wheelView!!.setWheelItemCount(size)
        val textView =  viewBinding.textView2 as TextView
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
        /*wheelView!!.onWheelItemClickListener =
            OnWheelItemClickListener { parent, position, isSelected ->
                Toast.makeText(
                    this@HomeMainFragment,
                    "you clicked$position",
                    Toast.LENGTH_SHORT
                ).show()
            }

           이거 makeText부분이 인식이 안됨 왜 안대는지는 모름
           근데 이거 모임? 왜9필요함
         */
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

        /*    wheelView.setOnWheelAngleChangeListener(new WheelView.OnWheelAngleChangeListener() {
            @Override
            public void onWheelAngleChange(float angle) {
                Toast.makeTextHomeMainFragment.this,"you change"+angle,Toast.LENGTH_SHORT).show();
            }
        });
*/
        /*
        val btn_history_album = viewBinding.btnHistoryAlbum
        btn_history_album.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, HistoryMainFragment::class.java)
            startActivity(intent)
        })

        val btn_home_album = viewBinding.btnHomeAlbum
        btn_home_album.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, HomeAlbumActivity::class.java)
            startActivity(intent)
        })

        이부부은 Fragment->Activity이기 떄문에 또 처리가 필요함

         */

        

        return viewBinding!!.root

    }
}

