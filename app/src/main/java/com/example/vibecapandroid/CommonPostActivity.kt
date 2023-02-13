package com.example.vibecapandroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.vibecapandroid.databinding.ActivityCommonPostBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.util.regex.Pattern



class CommonPostActivity  : AppCompatActivity() {
    private val viewBinding: ActivityCommonPostBinding by lazy{
        ActivityCommonPostBinding.inflate(layoutInflater)
    }
    // 변수설정
    var vibe_id : Int = 0
    var title :String? = null
    var body : String? = null
    var feeling_tag : String? = null
    var video_id: String?= null
    var post_id : Int = 0

    //신경써야할 Input값은 Intent로 vibe_id 하나만 이 activity를 호출하는 activity/fragment에서 넘겨줘서 받을것.
    //아래 vibe_id라고 선언된 것.
    //나머지 변수들은 activity에서 자동으로 호출 및 설정함

    override fun onCreate(savedInstanceState: Bundle?) {
        //여기부터 아래 10줄 절대 건들지 말것
        //생명주기 맞춰서 설정해놓은것들임
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        vibe_id=intent.getIntExtra("vibe_id",0)
        setProfileData()
        getOneVibeInfo()

        //글자수 제한 및 글자수 표기
        with(viewBinding){
            commonPostTitle.addTextChangedListener(object : TextWatcher {
                var maxText = ""
                override fun beforeTextChanged(pos: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    maxText = pos.toString()
                }
                override fun onTextChanged(pos: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(commonPostTitle.lineCount > 3){
                        Toast.makeText(this@CommonPostActivity,
                            "최대 3줄까지 입력 가능합니다.",
                            Toast.LENGTH_SHORT).show()
                        commonPostTitle.setText(maxText)
                        commonPostTitle.setSelection(commonPostTitle.length())
                        commonPostTitleCount.setText("${commonPostTitle.length()} / 32")
                    } else if(commonPostTitle.length() > 32){
                        Toast.makeText(this@CommonPostActivity, "최대 32자까지 입력 가능합니다.",
                            Toast.LENGTH_SHORT).show()
                        commonPostTitle.setText(maxText)
                        commonPostTitle.setSelection(commonPostTitle.length())
                        commonPostTitleCount.setText("${commonPostTitle.length()} / 32")
                    } else {
                        commonPostTitleCount.setText("${commonPostTitle.length()} / 32")
                    }
                }
                override fun afterTextChanged(p0: Editable?) {
                }
            })
            commonPostBody.addTextChangedListener(object : TextWatcher {
                var maxText = ""
                override fun beforeTextChanged(pos: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    maxText = pos.toString()
                }
                override fun onTextChanged(pos: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(commonPostBody.lineCount > 10){
                        Toast.makeText(this@CommonPostActivity,
                            "최대 10줄까지 입력 가능합니다.",
                            Toast.LENGTH_SHORT).show()
                        commonPostBody.setText(maxText)
                        commonPostBody.setSelection(commonPostBody.length())
                        commonPostBodyCount.setText("${commonPostBody.length()} / 140")
                    } else if(commonPostBody.length() > 140){
                        Toast.makeText(this@CommonPostActivity, "최대 140자까지 입력 가능합니다.",
                            Toast.LENGTH_SHORT).show()
                        commonPostBody.setText(maxText)
                        commonPostBody.setSelection(commonPostBody.length())
                        commonPostBodyCount.setText("${commonPostBody.length()} / 140")
                    } else {
                        commonPostBodyCount.setText("${commonPostBody.length()} / 140")
                    }
                }
                override fun afterTextChanged(p0: Editable?) {
                }
            })

        }


        //여기서 부터 커스텀가능함

        //완료 버튼
        viewBinding.commonPostFinish.setOnClickListener{
            title = viewBinding.commonPostTitle.text.toString()
            body  = viewBinding.commonPostBody.text.toString()
            callPostApi()

        }
        //x 버튼
        viewBinding.commonBackbtn.setOnClickListener{
            super.finish()
        }

        //여기까지 커스텀 가능함
    }
    private fun getYouTubeId(youTubeUrl: String): String? {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(youTubeUrl)
        return if (matcher.find()) {
            matcher.group()
        } else {
            "error"
        }
    }
    private fun setYoutube(){

        val youTubePlayerView: YouTubePlayerView =viewBinding.commonPostYoutubePlayer
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo((video_id!!)!!, 0F)
            }
        })
    }
    private fun setProfileData(){
        val apiService = retrofit.create(MypageApiInterface::class.java)
        apiService.getMypageCheck(userToken, MEMBER_ID).enqueue(object :Callback<CheckMypageResponse> {
            override fun onResponse(
                call: Call<CheckMypageResponse>,
                response: Response<CheckMypageResponse>
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
                                    "Result:${responseData.result.email}"+
                                    "Result:${responseData.result.profile_image}"+
                                    "Result:${responseData.result.nickname}"
                        )
                        viewBinding.commonPostUsername.text=responseData.result.nickname
                        Glide.with(this@CommonPostActivity).load(responseData.result.profile_image).into(viewBinding.commonPostProfile)
                    }
                    else{
                        Log.d("Retrofit","Null data") }

                } else {
                    Log.w("Retrofit", "Response Not Successful${response.code()}")
                }
            }
            override fun onFailure(call: Call<CheckMypageResponse>, t: Throwable) {
                Log.e("Retrofit","Error",t)
            }

        })
    }

    fun sortTag(unsortedTag :String):String?{
        val str=   unsortedTag?.split('"')
        Log.d("str","$str")
        val str2=str?.get(1)?.split(' ')
        return str2?.get(2)
    }


    fun callPostApi(){
        Log.d(
            "Input",
            "\n"+
                    "token:${userToken}\n " +
                    "member: ${member(MEMBER_ID.toInt())}\n" +
                    "Message:${title} \n" +
                    "Result:${body}"+
                    "Result:${vibe(vibe_id)}"
        )

        var member = member(MEMBER_ID.toInt())
        var vibe = vibe(vibe_id)

        Log.d("tagname ",feeling_tag.toString())
        Log.d("tagname",viewBinding.commonPostTagOwntype.text.toString())
        var owntag=viewBinding.commonPostTagOwntype.text.toString()
        val apiService = retrofit.create(PostApiInterface::class.java)
        if(owntag==null || owntag=="" || owntag==" " || owntag==" #"){
            owntag="#"
        }
        apiService.posting(
            userToken,  PostRequest(member,title,body,vibe, "$owntag")
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
                            //post_id 저장
                            post_id = responseData.result
                            Log.d("레트로핏",responseData.result.toString())
                            Toast.makeText(applicationContext, "게시물 작성 완료", Toast.LENGTH_LONG).show();
                            val nextIntent = Intent(this@CommonPostActivity, MypagePostActivity::class.java)
                            nextIntent.putExtra("post_id", post_id)
                            nextIntent.putExtra("vibe_id",vibe_id)
                            Log.d("postid",post_id.toString())
                            startActivity(nextIntent)
                            this@CommonPostActivity.finish()

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
    private fun getOneVibeInfo(){
        val apiService=retrofit.create(HistoryApiInterface::class.java)
        apiService.getHistoryOne(userToken, vibe_id.toLong())
            .enqueue(object : Callback<HistoryOneResponse> {
                override fun onResponse(call: Call<HistoryOneResponse>, response: Response<HistoryOneResponse>) {
                    val responseData=response.body()
                    if(response.isSuccessful){
                        if (responseData != null) {
                            Log.d(
                                "getHistoryOneResponse",
                                "getHistoryOneResponse\n"+
                                        "isSuccess:${responseData.is_success}\n " +
                                        "Code: ${responseData.code} \n" +
                                        "Message:${responseData.message} \n" +
                                        "Result:${responseData.result.vibe_id}"+
                                        "Result:${responseData.result.member_id}"+
                                        "Result:${responseData.result.vibe_image}"+
                                        "Result:${responseData.result.youtube_link}"+
                                        "Result:${responseData.result.vibe_keywords}"
                            )
                            if(responseData.is_success) {
                                video_id=getYouTubeId(responseData.result.youtube_link)
                                if(responseData.result.vibe_keywords!=null && sortTag(responseData.result.vibe_keywords)!="") {
                                    feeling_tag = sortTag(responseData.result.vibe_keywords)
                                    viewBinding.textViewTag1.setText("#"+feeling_tag)
                                }
                                Log.d("responseData feeling",responseData.result.vibe_keywords.toString())
                                //기분으로 태그 작성
                                setYoutube()
                            }
                            else{Log.d("getHistoryOne 통신 Fail","Fail Data is null")}
                        } else { Log.d("getHistoryOne","getHistoryOne Response Null data") }
                    } else{ Log.d("getHistoryOne","getHistoryOne Response Response Not Success") }
                } override fun onFailure(call: Call<HistoryOneResponse>, t: Throwable) { Log.d("getHistoryOne","${t.toString()}")}
            })
    }

}
