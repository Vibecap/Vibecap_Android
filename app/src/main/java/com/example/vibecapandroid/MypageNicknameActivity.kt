package com.example.vibecapandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.vibecapandroid.coms.ChangeNicknameResponse
import com.example.vibecapandroid.coms.CheckMypageResponse
import com.example.vibecapandroid.coms.MypageApiInterface
import com.example.vibecapandroid.coms.patchNickNameInput
import com.example.vibecapandroid.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MypageNicknameActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_nickname)

        val mypage_back = findViewById<ImageView>(R.id.activity_mypage_nickname_close)
        mypage_back.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MypageSetupActivity::class.java)
            startActivity(intent)
            finish()
        })

        val mypage_next = findViewById<Button>(R.id.activity_mypage_nickname_editdone)



        //웹 브라우저 창 열기
        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //어떤 주소로 들어갈지 입력
        val apiService = retrofit.create(MypageApiInterface::class.java)

        mypage_next.setOnClickListener(View.OnClickListener {
            apiService.patchNicknameChange(userToken, patchNickNameInput(MEMBER_ID,findViewById<EditText>(R.id.activity_mypage_nickname_edit).text.toString())).enqueue(object :
                Callback<ChangeNicknameResponse> {
                override fun onResponse(
                    call: Call<ChangeNicknameResponse>,
                    response: Response<ChangeNicknameResponse>
                ) {

                    if (response.isSuccessful) {
                        val responseData = response.body()

                        if (responseData !== null) {
                            Log.d(
                                "Retrofit",
                                "MypageNicknameResponse\n"+
                                        "isSuccess:${responseData.is_success}" +
                                        "Code:${responseData.code}"+
                                        "Message:${responseData.message}"+
                                        "Result:${responseData.result.nickname}"

                            )






                        }
                        else{
                            Log.d("Retrofit","Null data") }

                    } else {
                        Log.w("Retrofit", "Response Not Successful${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ChangeNicknameResponse>, t: Throwable) {
                    Log.e("Retrofit","Error",t)
                }

            })

            val intent = Intent(this, MypageSetupActivity::class.java)
            startActivity(intent)
        })

        Log.d("userToken","$userToken")
        //입력한 주소중 하나로 연결 시도




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