package com.example.vibecapandroid

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.vibecapandroid.coms.DeleteResponse
import com.example.vibecapandroid.coms.HomeApiInterface
import com.example.vibecapandroid.databinding.ActivityHistoryYoutubeBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
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
    private var position:Int?=0




    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        var img = intent.extras!!.getString("vibe_image")!!
        var uri = Uri.parse(img)
        vibeId=intent.extras!!.getInt("vibe_id")
        Log.d("vibe_id","${vibeId}")
        vibeKeyWords=intent.extras!!.getString("vibe_keywords")
        Log.d("vibe_keywords","$vibeKeyWords")
        videoID=intent.extras!!.getString("video_id")
        var deleteCounter=0
        position=intent.extras!!.getInt("position")
        Youtubeplay()

        viewBinding.btWrite.setOnClickListener(){
            if(vibeId!=null){
                val nextIntent = Intent(this, CommonPostActivity::class.java)
                nextIntent.putExtra("vibe_id", vibeId!!.toInt())
                startActivity(nextIntent)
            }
            else{
                Toast.makeText(applicationContext, "????????? ?????????????????? ????????? ????????????", Toast.LENGTH_SHORT).show();
            }
        }

        viewBinding.btHome.setOnClickListener(){
            val nextIntent = Intent(this, MainActivity::class.java)
            val fragcode=3
            nextIntent.putExtra("frag_code", fragcode)
            startActivity(nextIntent)
            super.finish()
        }

        //share button
        viewBinding.btShare.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, videoID)
            }
            startActivity(Intent.createChooser(intent, videoID))

        }
        viewBinding.btDelete.setOnClickListener{
            if(deleteCounter==0) {
                Log.d("receive position","$position")
                arrayList!!.removeAt(position!!)
                historyMainAdapters?.notifyItemRemoved(position!!)
                deletePhoto()
                deleteCounter=1
            }else if(deleteCounter==1){
                Toast.makeText(
                    applicationContext,
                    "?????? ????????? ?????? ???????????? ?????? ???????????????",
                    Toast.LENGTH_SHORT
                ).show()
                deleteCounter=2
            }
        }


    }
    /*override fun onPostResume() {
        super.onPostResume()

        // ?????????????????? ????????? ??????
        // ACTION_DOWNLOAD_COMPLETE : ??????????????? ??????????????? ??? ??????
        val completeFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(downloadCompleteReceiver, completeFilter)
    }
    override fun onPause() {
        super.onPause()
        unregisterReceiver(downloadCompleteReceiver)
    }*/


   /* override fun onRestart() {
        super.onRestart()
     //   YoutubePlayAgain()
    }
*/
    private fun deletePhoto() {
        //base url ??????
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
                            Log.d("????????????", responseData.result)
                            Toast.makeText(applicationContext, "?????? ?????? ??????", Toast.LENGTH_SHORT)
                                .show();
                        }
                        2100 -> {
                            Log.d("????????????", "?????? ???????????? ?????? ?????? ????????? ????????????")
                        }
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "?????? ????????? ?????? ???????????? ?????? ???????????????",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("????????????", "Response Not Success ${response.code()}")
                }
            }
            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                Log.d("????????????", "???????????? ?????? ??????" + t.message.toString())
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


   /* private fun YoutubePlayAgain(){
        var YoutubePlayerFragment = YoutubePlayerFragment.newInstance()
        var bundle = Bundle()
        bundle.putString("VIDEO_ID", getYouTubeId(videoID!!))
        YoutubePlayerFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.history_youtube_you_tube_player_view, YoutubePlayerFragment)
            .commitAllowingStateLoss()
    }
*/
    private fun Youtubeplay(){
        val youTubePlayerView: YouTubePlayerView =viewBinding.historyYoutubeYouTubePlayerView
        //lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(getYouTubeId(videoID!!)!!, 0F)
            }
        })
       youTubePlayerView.enableBackgroundPlayback(true)
    }
    override fun onBackPressed() {
        val nextIntent = Intent(this, MainActivity::class.java)
        val fragcode=3
        nextIntent.putExtra("frag_code", fragcode)
        startActivity(nextIntent)
        super.finish()
    }
/*
    //????????????
     open fun URLDownloading(url: Uri) {
        Log.d(TAG, "?????? URLDownloading")
        if (mDownloadManager == null) {
            mDownloadManager =
                this@HistoryYoutubeActivity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        }
        val outputFile = File(outputFilePath)
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs()
        }
        // DownloadManager.Request??? ???????????? DownloadManager Queue??? ???????????? ?????? ?????? ????????? ???????????? ??????????????? ????????????.
        // DownloadManager.Request : Request ????????? ???????????? ????????? ??????????????? ????????? URI??? ????????????.
        val request = DownloadManager.Request(url)
        val pathSegmentList = url.pathSegments
        // setTitle : notification ??????
        request.setTitle("???????????? ??????")
        request.setDescription("Downloading Dev Summit") //setDescription: ????????????????????? ????????? ????????????????????????.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) // setNotificationVisibility : VISIBILITY_VISIBLE??? ???????????? notification??? ????????????.
        request.setDestinationUri(Uri.fromFile(outputFile)) // setDestinationUri : ????????? ????????? ????????? URI
        request.setAllowedOverMetered(true)
        Toast.makeText(
            this@HistoryYoutubeActivity,
            "??????????????? ?????????????????????.",
            Toast.LENGTH_SHORT
        ).show()
        // DownloadManager ?????? ???????????? ???????????? ???????????? URI ????????? ?????????.
        mDownloadQueueId = mDownloadManager!!.enqueue(request)
    }

    // ???????????? ????????????
    private val downloadCompleteReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (mDownloadQueueId === reference) {
                val query = DownloadManager.Query() // ???????????? ?????? ????????? ????????? ?????? ??????
                query.setFilterById(reference)
                val cursor: Cursor = mDownloadManager!!.query(query)
                cursor.moveToFirst()
                val columnIndex: Int = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val columnReason: Int = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
                val status: Int = cursor.getInt(columnIndex)
                val reason: Int = cursor.getInt(columnReason)
                cursor.close()
                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        Log.d("HistoryYoutube","Download completede")}
                    DownloadManager.STATUS_PAUSED -> Toast.makeText(
                        this@HistoryYoutubeActivity,
                        "??????????????? ?????????????????????.",
                        Toast.LENGTH_SHORT
                    ).show()
                    DownloadManager.STATUS_FAILED -> Toast.makeText(
                        this@HistoryYoutubeActivity,
                        "??????????????? ?????????????????????.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // ????????? ??????
    private fun galleryAddPic(Image_Path: String) {
        Log.d(TAG, "?????? ????????? ?????? : $Image_Path")

        // ?????? ?????? ??????
        *//*Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    File f = new File(Image_Path);
    Uri contentUri = Uri.fromFile(f);
    mediaScanIntent.setData(contentUri);
    this.context.sendBroadcast(mediaScanIntent);*//*
        val file = File(Image_Path)
        MediaScannerConnection.scanFile(
            this, arrayOf(file.toString()),
            null, null
        )
    }*/


}