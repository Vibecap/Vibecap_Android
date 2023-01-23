package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.prnd.YouTubePlayerView

class YoutubePlayerFragment : Fragment(){

    val VIDEO_ID:String = arguments?.getString("VIDEO_ID")!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_youtube_player, container, false)

    @SuppressLint("SuspiciousIndentation")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val youTubePlayerView = view?.findViewById<YouTubePlayerView>(R.id.you_tube_player_view_fragment)
            youTubePlayerView?.play(VIDEO_ID)

    }

    companion object {
        fun newInstance() = YoutubePlayerFragment()

        //private const val VIDEO_ID = "m2SZ6RV_J7I"
    }
}




