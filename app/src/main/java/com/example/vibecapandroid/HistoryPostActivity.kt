package com.example.vibecapandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.vibecapandroid.coms.*
import com.example.vibecapandroid.databinding.ActivityHistoryPostBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryPostActivity : AppCompatActivity() {
    private val viewBinding : ActivityHistoryPostBinding by lazy {
        ActivityHistoryPostBinding.inflate(layoutInflater)
    }
    val apiService = retrofit.create(HistoryApiInterface::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val vibeId=intent.extras!!.getInt("vibe_id")
        val tagName=intent.extras!!.getString("vibe_keywords")
        val title:String=viewBinding.activityHistoryPostTitle.text.toString()
        val body:String=viewBinding.activityHistoryPostText.text.toString()
        Log.d("posting vibeID","$vibeId")
        Log.d("posting video_keyword","$tagName")

        viewBinding.textViewTag1.setText(tagName)

        viewBinding.activityHistoryPostFinish.setOnClickListener(){
            apiService.postHistoryPosting(userToken,
                HistoryPostingBody(memberIdClass(MEMBER_ID.toInt()),title,body ,vibeIDClass(vibeId), viewBinding.textViewTag1.text.toString())
            ).enqueue(object : Callback<HistoryPostingResponse> {
                    override fun onResponse(call: Call<HistoryPostingResponse>, response: Response<HistoryPostingResponse>) {
                        val responseData=response.body()
                        Log.d("tag", "postHistoryResponse\n"+
                                "isSuccess:${responseData?.is_success}\n " +
                                "Code: ${responseData?.code} \n" +
                                "Message:${responseData?.message} \n" +
                                "Result:${responseData?.result}")
                        if(response.isSuccessful){
                            if (responseData != null) {
                                Log.d("tag", "postHistoryResponse\n"+
                                            "isSuccess:${responseData.is_success}\n " +
                                            "Code: ${responseData.code} \n" +
                                            "Message:${responseData.message} \n" +
                                            "Result:${responseData.result}")
                                if(responseData.is_success) {
                                    Log.d("postHistoryResponse 통신 success","success")
                                }
                                else{
                                    Log.d("postHistoryResponse 통신 Fail","Fail Data is null")
                                }
                            } else
                            {
                                Log.d("postHistoryResponse","getHistoryOne Response Null data")
                            }
                        } else{
                            Log.d("postHistoryResponse","getHistoryOne Response Response Not Success")
                        }
                    } override fun onFailure(call: Call<HistoryPostingResponse>, t: Throwable) { Log.d("postHistoryResponse","${t.toString()}")}
                })}



        // 게시물 내용 서버에 전달하기
    }
}