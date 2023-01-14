package com.example.vibecapandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class HomeCapturedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_home_captured)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.you_tube_player_view, YoutubePlayerFragment.newInstance())
                .commitNow()
        }
    }

}