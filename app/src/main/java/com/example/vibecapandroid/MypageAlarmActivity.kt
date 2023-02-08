package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.vibecapandroid.coms.MypageApiInterface
import com.example.vibecapandroid.coms.getAlarmHistoryResponse
import com.example.vibecapandroid.databinding.ActivityCommonPostBinding
import com.example.vibecapandroid.databinding.ActivityMypageAlarmBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageAlarmActivity : AppCompatActivity() {

    private val viewBinding: ActivityMypageAlarmBinding by lazy{
        ActivityMypageAlarmBinding.inflate(layoutInflater)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_alarm)

        val mypage_back = findViewById<ImageView>(R.id.activity_mypage_alarm_back)
        mypage_back.setOnClickListener(View.OnClickListener {
            finish()
        })

     /*   val alarmList = arrayListOf(
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_like,"내가 작성한 게시물을 좋아요 했습니다","user123","님이 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user123","노래가.좋네요.우리.임영웅..."),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user456","노래 분위기가 너무 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_like,"내가 작성한 게시물을 좋아요 했습니다","user123","님이 좋아요"),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user123","노래가.좋네요.우리.임영웅..."),
            MypageAlarmClass(R.drawable.ic_activity_mypage_alarm_comment,"내가 작성한 게시물에 댓글이 달렸습니다","user456","노래 분위기가 너무 좋아요"),

            )*/

        val apiService = retrofit.create(MypageApiInterface::class.java)
        //입력한 주소중 하나로 연결 시도
        apiService.getAlarmHistory(userToken, MEMBER_ID).enqueue(object :
            Callback<getAlarmHistoryResponse> {
            @SuppressLint("ResourceType")
            override fun onResponse(
                call: Call<getAlarmHistoryResponse>,
                response: Response<getAlarmHistoryResponse>
            ) {

                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData !== null) {
                        Log.d(
                            "Retrofit",
                            "MypageResponse\n"+
                                    "isSuccess:${responseData.is_success}" +
                                    "Code:${responseData.code}"+
                                    "Message:${responseData.message}"+
                                    "Result:${responseData.result}"
                        )
                        if(responseData.is_success){
                            if(responseData.result.isEmpty()){
                                Log.d("활동내역 없음","활동내역 없음")
                            }
                            else{

                            }
                        }

                    }
                    else{
                        Log.d("Retrofit","Null data") }

                } else {
                    Log.w("Retrofit", "Response Not Successful${response.code()}")
                }
            }

            override fun onFailure(call: Call<getAlarmHistoryResponse>, t: Throwable) {
                Log.e("Retrofit","Error",t)
            }

        })

       /* val activity_mypage_alarm_recyclerview = findViewById<RecyclerView>(R.id.activity_mypage_alarm_recyclerview)
        activity_mypage_alarm_recyclerview.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        activity_mypage_alarm_recyclerview.setHasFixedSize(true)

        activity_mypage_alarm_recyclerview.adapter = MypageAlarmadaptersClass(alarmList)*/
/*
        activity_mypage_alarm_recyclerview.addItemDecoration(
            DividerItemDecoration(this,DividerItemDecoration.VERTICAL)
        )
*/
    }

}
