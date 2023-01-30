package com.example.vibecapandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        Youtubeplay()

        viewBinding.btWrite.setOnClickListener(){
            val intent = Intent(this, HistoryPostActivity::class.java)
            startActivity(intent)
        }

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

    private fun Youtubeplay(){
        //uri intent로 가져오기
        //val imageString = intent.getStringExtra("uri")
        //val uri: Uri = Uri.parse(imageString)

        val videoID=intent.extras!!.getString("video_id")
        Log.d("history youtube id",videoID.toString())
        var YoutubePlayerFragment = YoutubePlayerFragment.newInstance()
        var bundle = Bundle()
        Log.d("Video ID","${getYouTubeId(videoID!!)}")
        bundle.putString("VIDEO_ID", getYouTubeId(videoID!!))
        YoutubePlayerFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.history_youtube_you_tube_player_view, YoutubePlayerFragment)
            .commitNow()

    }


}