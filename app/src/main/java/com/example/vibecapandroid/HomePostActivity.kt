package com.example.vibecapandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vibecapandroid.coms.*
import com.example.vibecapandroid.databinding.ActivityHomePostBinding
import com.google.android.youtube.player.internal.l
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePostActivity  : AppCompatActivity() {


    private val viewBinding: ActivityHomePostBinding by lazy{
        ActivityHomePostBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = viewBinding.root
        setContentView(view)
        //기분으로 태그 작성
        viewBinding.textViewTag1.setText(feeling)
        
        //완료 버튼
        viewBinding.activityHistoryPostFinish.setOnClickListener{

            //post request 객체 생성
            var member_id :Int  = MEMBER_ID.toInt()
            var member =member(member_id)
            var vibe_id : Int = intent.getLongExtra("vibe_id",0).toInt()
            var vibe = vibe(vibe_id)
            var title :String = viewBinding.editTextTitle.text.toString()
            var body : String = viewBinding.editTextPostbody.text.toString()
            var tag : String = feeling


            Log.d("postrequest",PostRequest(member, title,body,vibe,tag).toString())
            //api 연결
            val apiService = retrofit.create(PostApiInterface::class.java)
            apiService.posting(
                userToken,  PostRequest(member, title,body,vibe,tag)


            ).enqueue(object : Callback<PostResponse> {
                override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                    val responseData = response.body()
                    Log.d(
                        "post",
                        "post\n"+
                                "isSuccess:${responseData?.is_success}\n " +
                                "Code: ${responseData?.code} \n" +
                                "Message:${responseData?.message} \n" )
                    if (responseData?.is_success==true) {
                        when(response.body()?.code){
                            1000 ->{
                                Log.d("레트로핏",responseData.result.toString())
                                Toast.makeText(applicationContext, "게시물 작성 완료", Toast.LENGTH_LONG).show();
                                //게시물 조회 액티비티로 이동
                            }
                            500 -> {
                                Log.d ("레트로핏","해당 바이브에 대한 접근 권한이 없습니다" )
                            }
                            2011 -> {
                                Log.d("레트로핏", "제목의 글자수를 확인해 주세요(32자 이하 인지 확인)")
                            }
                            2012 -> {
                                Log.d("레트로핏","내용의 글자수를 확인해주세요(140자 이하 인지 확인)")
                            }

                        }
                    }
                    else {
                        Toast.makeText(applicationContext, "게시물 작성 실패", Toast.LENGTH_LONG).show()
                        Log.d("레트로핏","Response Not Success ${response.code()}")
                    }


                }

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                        Log.d("레트로핏","레트로핏 호출 실패" +t.message.toString())

                }


            })
        }


        //x 버튼
        viewBinding.imageButtonBack.setOnClickListener{


            val nextIntent = Intent(this, VibePostActivity::class.java)
            //nextIntent.putExtra("video_id",video_id)
           // nextIntent.putExtra("vibe_id", vibe_id!!.toInt())
            startActivity(nextIntent)
            finish()

        }





        var video_id: String? = null
        video_id = intent.getStringExtra("video_id")
        var YoutubePlayerFragment = YoutubePlayerFragment.newInstance()
        var bundle = Bundle()
        bundle.putString("VIDEO_ID", video_id)
        YoutubePlayerFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_post_you_tube_player_view, YoutubePlayerFragment)
            .commitNow()


    }


}
