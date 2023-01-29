package com.example.vibecapandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vibecapandroid.databinding.ActivityHomePostBinding

class HomePostActivity  : AppCompatActivity() {

    var video_id: String? = null
    private val viewBinding: ActivityHomePostBinding by lazy{
        ActivityHomePostBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = viewBinding.root
        setContentView(view)




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
