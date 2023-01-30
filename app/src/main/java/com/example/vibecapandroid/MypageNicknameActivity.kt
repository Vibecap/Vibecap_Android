package com.example.vibecapandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.vibecapandroid.databinding.ActivityMainBinding

class MypageNicknameActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_nickname)

        val mypage_back = findViewById<ImageView>(R.id.activity_mypage_nickname_close)
        mypage_back.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MypageSetupActivity::class.java)
            startActivity(intent)
        })

        val mypage_next = findViewById<Button>(R.id.activity_mypage_nickname_editdone)
        mypage_next.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MypageSetupActivity::class.java)
            startActivity(intent)
        })
        findViewById<EditText>(R.id.activity_mypage_nickname_edit).text.toString()

        with(binding){
            val editText =findViewById<EditText>(R.id.activity_mypage_nickname_edit)
            val textinfo = findViewById<TextView>(R.id.activity_mypage_nickname_info)
            editText.addTextChangedListener(object :TextWatcher{

                override fun beforeTextChanged(position: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(position: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(editText.length()>14){
                        textinfo.setText("글자수가 너무 많습니다.")
                        val color= getColor(R.color.red)
                        textinfo.setTextColor(color)
                        val button: Button = findViewById(R.id.activity_mypage_nickname_editdone)

                    }
                    else if(editText.length()<4){
                        textinfo.setText("글자수가 너무 적습니다.")
                        val color= getColor(R.color.red)
                        textinfo.setTextColor(color)
                        val button: Button = findViewById(R.id.activity_mypage_nickname_editdone)

                    }
                    else{
                        textinfo.setText("사용 가능한 닉네임 입니다.")
                        val backgroundcolor = getColor(R.color.back_txt)
                        val textcolor = getColor(R.color.black)
                        textinfo.setTextColor(textcolor)

                        val button: Button = findViewById(R.id.activity_mypage_nickname_editdone)
                        button.setBackgroundColor(backgroundcolor)
                        button.setTextColor(textcolor)


                    }

                }

                override fun afterTextChanged(position: Editable?) {

                }
            })
        }
    }
}