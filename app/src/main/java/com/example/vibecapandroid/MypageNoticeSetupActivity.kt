package com.example.vibecapandroid

import android.R.id.toggle
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vibecapandroid.databinding.ActivityMypageNoticesetupBinding


class MypageNoticeSetupActivity : AppCompatActivity() {

    private val viewBinding: ActivityMypageNoticesetupBinding by lazy{
        ActivityMypageNoticesetupBinding.inflate(layoutInflater)
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        var editor = getSharedPreferences(
            "sharedprefs",
            Context.MODE_PRIVATE
        ).edit()

        viewBinding.activityMypageNicknameClose.setOnClickListener(){
            finish()
        }

        viewBinding.switch4.isChecked =
            getSharedPreferences("sharedprefs",Context.MODE_PRIVATE).getBoolean("setImageOnly",false)


            viewBinding.switch4.setOnCheckedChangeListener { buttonView, isChecked ->
                Log.d("Tag", "IsChecked")
                if (isChecked) {
                    Toast.makeText(this, "사진 인식 우선모드가 활성화되었습니다.", Toast.LENGTH_SHORT).show()
                  //  setOnlyUseImageOnCapture=true
                    editor.putBoolean("setImageOnly", true)
                    editor.apply()
                } else {
                    editor.remove("setImageOnly")
                    editor.apply()
                  //  setOnlyUseImageOnCapture=false
                }
                getSharedPreferences("sharedprefs",Context.MODE_PRIVATE).getBoolean("setImageOnly",false)
            }

    }

}