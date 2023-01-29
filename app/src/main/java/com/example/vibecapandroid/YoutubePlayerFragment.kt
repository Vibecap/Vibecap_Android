package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.os.Build.ID
import android.os.Bundle
import android.provider.MediaStore.Video.Thumbnails.VIDEO_ID
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.youtube.player.internal.q
import kr.co.prnd.YouTubePlayerView

class YoutubePlayerFragment() : Fragment(){

   var VIDEO_ID:String? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_youtube_player, container, false)

    @SuppressLint("SuspiciousIndentation")
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        VIDEO_ID= arguments?.getString("VIDEO_ID")

        val youTubePlayerView = view?.findViewById<YouTubePlayerView>(R.id.you_tube_player_view_fragment)

            if(VIDEO_ID == null){
                Log.d("레트로핏", "YoutubePlayerFragment VIDEO ID 받기 실패 "+ "video_id 없음")
            }
            else {
                youTubePlayerView?.play(VIDEO_ID!!)
                Log.d("레트로핏", "YoutubePlayerFragment VIDEO ID 받기 성공 "+ VIDEO_ID!!)
            }
        super.onActivityCreated(savedInstanceState)
    }



    companion object {
        fun newInstance() = YoutubePlayerFragment()

        //private val VIDEO_ID = "m2SZ6RV_J7I"
    }
}




