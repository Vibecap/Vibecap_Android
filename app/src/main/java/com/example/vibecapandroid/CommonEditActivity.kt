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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
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
                            "?????? 3????????? ?????? ???????????????.",
                            Toast.LENGTH_SHORT).show()
                        commonPostTitle.setText(maxText)
                        commonPostTitle.setSelection(commonPostTitle.length())
                        commonPostTitleCount.setText("${commonPostTitle.length()} / 32")
                    } else if(commonPostTitle.length() > 32){
                        Toast.makeText(this@CommonEditActivity, "?????? 32????????? ?????? ???????????????.",
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
                            "?????? 10????????? ?????? ???????????????.",
                            Toast.LENGTH_SHORT).show()
                        commonPostBody.setText(maxText)
                        commonPostBody.setSelection(commonPostBody.length())
                        commonPostBodyCount.setText("${commonPostBody.length()} / 140")
                    } else if(commonPostBody.length() > 140){
                        Toast.makeText(this@CommonEditActivity, "?????? 140????????? ?????? ???????????????.",
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
                            //post_id ??????
                            Toast.makeText(applicationContext, "????????? ?????? ??????", Toast.LENGTH_LONG).show();
                            /*val nextIntent = Intent(this@CommonEditActivity, MypagePostActivity::class.java)
                            nextIntent.putExtra("post_id", postId)
                            Log.d("postid",postId.toString())
                            startActivity(nextIntent)*/
                            //????????? activity ???????????????????????? ???????????? restart ???????????????
                            this@CommonEditActivity.finish()
                        }
                        500 -> {
                            Log.d ("????????????","?????? ???????????? ?????? ?????? ????????? ????????????" )
                        }
                        2011 -> {
                            Log.d("????????????", "????????? ???????????? ????????? ?????????(32??? ?????? ?????? ??????)")
                        }
                        2012 -> {
                            Log.d("????????????","????????? ???????????? ??????????????????(140??? ?????? ?????? ??????)")
                        }
                    }
                }
                else {
                    Toast.makeText(applicationContext, "????????? ?????? ??????", Toast.LENGTH_LONG).show()
                    Log.d("????????????","Response Not Success ${response.code()}")
                }
            }
            override fun onFailure(call: Call<patchMypageEditResponse>, t: Throwable) {
                Log.d("????????????","???????????? ?????? ??????" +t.message.toString())
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
        val youTubePlayerView: YouTubePlayerView =viewBinding.commonPostYoutubePlayer
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo((video_id!!)!!, 0F)
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
                                //???????????? ?????? ??????
                              //  viewBinding.textViewTag1.setText("#"+feeling_tag)
                                setYoutube()
                            }
                            else{Log.d("getHistoryOne ?????? Fail","Fail Data is null")}
                        } else { Log.d("getHistoryOne","getHistoryOne Response Null data") }
                    } else{ Log.d("getHistoryOne","getHistoryOne Response Response Not Success") }
                } override fun onFailure(call: Call<HistoryOneResponse>, t: Throwable) { Log.d("getHistoryOne","${t.toString()}")}
            })
    }
    /*** ????????? 1??? ?????? */
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

                    // ?????? response ??? code ?????? ?????? ??????
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