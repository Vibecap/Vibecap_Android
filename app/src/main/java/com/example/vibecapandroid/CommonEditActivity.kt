package com.example.vibecapandroid

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.*
import com.example.vibecapandroid.databinding.ActivityCommonEditBinding
import com.example.vibecapandroid.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class CommonEditActivity : AppCompatActivity() {

    private val viewBinding: ActivityCommonEditBinding by lazy{
        ActivityCommonEditBinding.inflate(layoutInflater)
    }
    var title :String? = null
    var body : String? = null
    var post_id : Int = 0
    var feeling_tag : String? = null
    var video_id: String?= null
    var vibe_id:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        vibe_id=intent.getIntExtra("vibe_id",0)
        post_id = intent.extras!!.getInt("post_id")
        setProfileData()
        getOneVibeInfo()
        getPost(post_id, MEMBER_ID)


        with(viewBinding){
            commonPostTitle.addTextChangedListener(object : TextWatcher {
                var maxText = ""
                override fun beforeTextChanged(pos: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    maxText = pos.toString()
                }
                override fun onTextChanged(pos: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(commonPostTitle.lineCount > 3){
                        Toast.makeText(this@CommonEditActivity,
                            "최대 3줄까지 입력 가능합니다.",
                            Toast.LENGTH_SHORT).show()
                        commonPostTitle.setText(maxText)
                        commonPostTitle.setSelection(commonPostTitle.length())
                        commonPostTitleCount.setText("${commonPostTitle.length()} / 32")
                    } else if(commonPostTitle.length() > 32){
                        Toast.makeText(this@CommonEditActivity, "최대 32자까지 입력 가능합니다.",
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
                        Toast.makeText(this@CommonEditActivity,
                            "최대 10줄까지 입력 가능합니다.",
                            Toast.LENGTH_SHORT).show()
                        commonPostBody.setText(maxText)
                        commonPostBody.setSelection(commonPostBody.length())
                        commonPostBodyCount.setText("${commonPostBody.length()} / 140")
                    } else if(commonPostBody.length() > 140){
                        Toast.makeText(this@CommonEditActivity, "최대 140자까지 입력 가능합니다.",
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
        viewBinding.commonEditFinish.setOnClickListener{
            title = viewBinding.commonPostTitle.text.toString()
            body  = viewBinding.commonPostBody.text.toString()
            callEditApi()
        }
        viewBinding.commonBackbtn.setOnClickListener {
            val dialogBinding = layoutInflater.inflate(R.layout.activity_history_postedit_dialog_cancel,null)

            val cancelDialog = Dialog(this)
            cancelDialog.setContentView(dialogBinding)
            cancelDialog.setCancelable(true)
            cancelDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            cancelDialog.show()


            val cancelBtn = dialogBinding.findViewById<Button>(R.id.dialog_postdedit_cancel)
            cancelBtn.setOnClickListener {
                cancelDialog.dismiss()
            }
            val okayBtn = dialogBinding.findViewById<Button>(R.id.dialog_postedit_ok)
            okayBtn.setOnClickListener {
                if (cancelDialog != null && cancelDialog!!.isShowing) {
                    cancelDialog!!.dismiss()
                }
                super.finish()
            }

        }



    }
    private fun setProfileData(){
        val apiService = retrofit.create(MypageApiInterface::class.java)
        apiService.getMypageCheck(userToken, MEMBER_ID).enqueue(object :
            Callback<CheckMypageResponse> {
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
                        Glide.with(this@CommonEditActivity).load(responseData.result.profile_image).into(viewBinding.commonPostProfile)
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
    fun callEditApi(){
        Log.d("vibe_id","${vibe_id}")
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


        val apiService = retrofit.create(MypageApiInterface::class.java)
        apiService.patchMypageEditPost(
            userToken,  post_id, patchMypageEditPostInput(member,title,body)
        ).enqueue(object : Callback<patchMypageEditResponse> {
            override fun onResponse(call: Call<patchMypageEditResponse>, response: Response<patchMypageEditResponse>) {
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
                            Toast.makeText(applicationContext, "게시물 수정 완료", Toast.LENGTH_LONG).show();
                            /*val nextIntent = Intent(this@CommonEditActivity, MypagePostActivity::class.java)
                            nextIntent.putExtra("post_id", postId)
                            Log.d("postid",postId.toString())
                            startActivity(nextIntent)*/
                            //새로운 activity 실행시키는것보다 생명주기 restart 이용해야함
                            this@CommonEditActivity.finish()
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
            override fun onFailure(call: Call<patchMypageEditResponse>, t: Throwable) {
                Log.d("레트로핏","레트로핏 호출 실패" +t.message.toString())
            }
        })
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


        var YoutubePlayerFragment = YoutubePlayerFragment.newInstance()
        var bundle = Bundle()
        bundle.putString("VIDEO_ID", video_id)
        YoutubePlayerFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.common_post_youtube_player, YoutubePlayerFragment)
            .commitNow()
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
                                        "Result:${responseData.result?.vibe_id}"+
                                        "Result:${responseData.result?.member_id}"+
                                        "Result:${responseData.result?.vibe_image}"+
                                        "Result:${responseData.result?.youtube_link}"+
                                        "Result:${responseData.result?.vibe_keywords}"
                            )
                            if(responseData.is_success) {
                                video_id=getYouTubeId(responseData.result.youtube_link)
                                //feeling_tag=responseData.result.vibe_keywords
                                Log.d("responseData",responseData.result.vibe_keywords.toString())
                                //기분으로 태그 작성
                              //  viewBinding.textViewTag1.setText("#"+feeling_tag)
                                setYoutube()
                            }
                            else{Log.d("getHistoryOne 통신 Fail","Fail Data is null")}
                        } else { Log.d("getHistoryOne","getHistoryOne Response Null data") }
                    } else{ Log.d("getHistoryOne","getHistoryOne Response Response Not Success") }
                } override fun onFailure(call: Call<HistoryOneResponse>, t: Throwable) { Log.d("getHistoryOne","${t.toString()}")}
            })
    }
    /*** 게시물 1개 조회 */
    private fun getPost(postId: Int, memberId: Long) {
        val vibePostService = getRetrofit().create(VibePostApiInterface::class.java)
        vibePostService.postDetailCheck(userToken, postId, memberId)
            .enqueue(object : Callback<PostDetailResponse> {
                override fun onResponse(
                    call: Call<PostDetailResponse>,
                    response: Response<PostDetailResponse>
                ) {
                    Log.d("[VIBE] GET_POST/SUCCESS", response.toString())
                    val resp: PostDetailResponse = response.body()!!

                    Log.d("[VIBE] GET_POST/CODE", resp.code.toString())

                    // 서버 response 중 code 값에 따른 결과
                    when (resp.code) {
                        1010, 1011, 1012, 1013 -> {
                         viewBinding.commonPostTitle.setText(resp.result.title)
                            viewBinding.commonPostBody.setText(resp.result.body)

                        }
                        else -> Log.d("EditPost Search Fail","Fail" )
                    }
                }

                override fun onFailure(call: Call<PostDetailResponse>, t: Throwable) {
                    Log.d("[VIBE] GET_POST/FAILURE", t.message.toString())
                }
            })
        Log.d("[VIBE] GET_POST", "HELLO")
    }


}