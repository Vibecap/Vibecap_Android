package com.example.vibecapandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vibecapandroid.coms.*
import com.example.vibecapandroid.databinding.ActivityHistoryYoutubeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern


class HistoryYoutubeActivity:AppCompatActivity() {

    private val viewBinding: ActivityHistoryYoutubeBinding by lazy{
        ActivityHistoryYoutubeBinding.inflate(layoutInflater)
    }
    private var vibeId:Int?=null
    private var vibeKeyWords:String?=null
    private var videoID:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        vibeId=intent.extras!!.getInt("vibe_id")
        Log.d("vibe_id","${vibeId}")
        vibeKeyWords=intent.extras!!.getString("vibe_keywords")
        Log.d("vibe_keywords","$vibeKeyWords")
        videoID=intent.extras!!.getString("video_id")

        val position=intent.extras!!.getInt("position")

        Youtubeplay()


        viewBinding.btWrite.setOnClickListener(){
            if(vibeId!=null){
                val nextIntent = Intent(this, CommonPostActivity::class.java)
                nextIntent.putExtra("vibe_id", vibeId!!.toInt())
                startActivity(nextIntent)
            }
            else{
                Toast.makeText(applicationContext, "서버에 저장되어있는 사진이 없습니다", Toast.LENGTH_SHORT).show();
            }
        }

        viewBinding.btHome.setOnClickListener(){
            finish() }

        //share button
        viewBinding.btShare.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, videoID)
            }
            startActivity(Intent.createChooser(intent, videoID))

        }
        viewBinding.btDelete.setOnClickListener{
            arrayList!!.removeAt(position)
            historyMainAdapters?.notifyItemRemoved(position)
            deletePhoto()
        }

    }

    override fun onRestart() {
        super.onRestart()
        YoutubePlayAgain()
    }

    private fun deletePhoto() {
        //base url 설정
        val apiService = retrofit.create(HomeApiInterface::class.java)
        apiService.deletephoto(userToken, vibeId?.toLong()).enqueue(
            object : Callback<DeleteResponse> {
            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>
            ) {
                val responseData = response.body()
                Log.d(
                    "deletephoto",
                    "deletephoto\n" +
                            "isSuccess:${responseData?.is_success}\n " +
                            "Code: ${responseData?.code} \n" +
                            "Message:${responseData?.message} \n"
                )
                if (responseData?.is_success == true) {
                    when (response.body()?.code) {
                        1000 -> {
                            vibeId = null
                            Log.d("레트로핏", responseData.result)
                            Toast.makeText(applicationContext, "사진 삭제 성공", Toast.LENGTH_SHORT)
                                .show();
                        }
                        2100 -> {
                            Log.d("레트로핏", "해당 바이브에 대한 접근 권한이 없습니다")
                        }
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "해당 사진이 이미 서버에서 삭제 되었습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("레트로핏", "Response Not Success ${response.code()}")
                }
            }
            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                Log.d("레트로핏", "레트로핏 호출 실패" + t.message.toString())
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


    private fun YoutubePlayAgain(){

        var YoutubePlayerFragment = YoutubePlayerFragment.newInstance()
        var bundle = Bundle()
        bundle.putString("VIDEO_ID", getYouTubeId(videoID!!))
        YoutubePlayerFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.history_youtube_you_tube_player_view, YoutubePlayerFragment)
            .commitAllowingStateLoss()
    }
    private fun Youtubeplay(){
        var YoutubePlayerFragment = YoutubePlayerFragment.newInstance()
        var bundle = Bundle()
        bundle.putString("VIDEO_ID", getYouTubeId(videoID!!))
        YoutubePlayerFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.history_youtube_you_tube_player_view, YoutubePlayerFragment)
            .commitNow()
    }

}