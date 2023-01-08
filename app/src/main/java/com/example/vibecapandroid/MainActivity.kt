package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Button
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.lukedeighton.wheelview.WheelView
import com.lukedeighton.wheelview.adapter.WheelAdapter

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private var wheelView: WheelView? = null
    private val colors = arrayOf("#fd5308", "#fd5308", "#fd5308", "#fd5308", "#fd5308", "#fd5308","#fd5308","#fd5308","#fd5308","#fd5308","#fd5308","#fd5308")
    var size = 12
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        wheelView = findViewById<View>(R.id.wheelview) as WheelView
        wheelView!!.setWheelItemCount(size)
        val shapeDrawables = arrayOfNulls<ShapeDrawable>(size)
        for (i in 0 until size) {
            shapeDrawables[i] = ShapeDrawable(OvalShape())
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
            WheelView.OnWheelItemClickListener { parent, position, isSelected ->
                Toast.makeText(
                    this@MainActivity,
                    "You Clicked",
                    Toast.LENGTH_SHORT
                ).show()
            }
        val btn_history_album = findViewById<Button>(R.id.btn_history_album)
        btn_history_album.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, HistoryAlbumActivity::class.java)
            startActivity(intent)
        })

        val btn_home_album = findViewById<Button>(R.id.btn_home_album)
        btn_home_album.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, HomeAlbumActivity::class.java)
            startActivity(intent)
        })
    }
}