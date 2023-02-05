package com.example.vibecapandroid

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.vibecapandroid.coms.DeleteResponse
import com.example.vibecapandroid.coms.HomeApiInterface
import com.example.vibecapandroid.databinding.ActivityHistoryYoutubeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
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
                Toast.makeText(applicationContext, "서버에 저장되어있는 사진이 없습니다", Toast.LENGTH_SHORT).show();
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
                    "해당 사진이 이미 서버에서 삭제 되었습니다",
                    Toast.LENGTH_SHORT
                ).show()
                deleteCounter=2
            }
        }


    }
    /*override fun onPostResume() {
        super.onPostResume()

        // 브로드캐스트 리시버 등록
        // ACTION_DOWNLOAD_COMPLETE : 다운로드가 완료되었을 때 전달
        val completeFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(downloadCompleteReceiver, completeFilter)
    }
    override fun onPause() {
        super.onPause()
        unregisterReceiver(downloadCompleteReceiver)
    }*/


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
    override fun onBackPressed() {
        val nextIntent = Intent(this, MainActivity::class.java)
        val fragcode=3
        nextIntent.putExtra("frag_code", fragcode)
        startActivity(nextIntent)
        super.finish()
    }
/*
    //스틱코드
     open fun URLDownloading(url: Uri) {
        Log.d(TAG, "태그 URLDownloading")
        if (mDownloadManager == null) {
            mDownloadManager =
                this@HistoryYoutubeActivity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        }
        val outputFile = File(outputFilePath)
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs()
        }
        // DownloadManager.Request을 설정하여 DownloadManager Queue에 등록하게 되면 큐에 들어간 순서대로 다운로드가 처리된다.
        // DownloadManager.Request : Request 객체를 생성하며 인자로 다운로드할 파일의 URI를 전달한다.
        val request = DownloadManager.Request(url)
        val pathSegmentList = url.pathSegments
        // setTitle : notification 제목
        request.setTitle("다운로드 항목")
        request.setDescription("Downloading Dev Summit") //setDescription: 노티피케이션에 보이는 디스크립션입니다.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) // setNotificationVisibility : VISIBILITY_VISIBLE로 설정되면 notification에 보여진다.
        request.setDestinationUri(Uri.fromFile(outputFile)) // setDestinationUri : 파일이 저장될 위치의 URI
        request.setAllowedOverMetered(true)
        Toast.makeText(
            this@HistoryYoutubeActivity,
            "다운로드를 완료하였습니다.",
            Toast.LENGTH_SHORT
        ).show()
        // DownloadManager 객체 생성하여 다운로드 대기열에 URI 객체를 넣는다.
        mDownloadQueueId = mDownloadManager!!.enqueue(request)
    }

    // 다운로드 상태조회
    private val downloadCompleteReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (mDownloadQueueId === reference) {
                val query = DownloadManager.Query() // 다운로드 항목 조회에 필요한 정보 포함
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
                        "다운로드가 중단되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    DownloadManager.STATUS_FAILED -> Toast.makeText(
                        this@HistoryYoutubeActivity,
                        "다운로드가 취소되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // 갤러리 갱신
    private fun galleryAddPic(Image_Path: String) {
        Log.d(TAG, "태그 갤러리 갱신 : $Image_Path")

        // 이전 사용 방식
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