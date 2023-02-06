package com.example.vibecapandroid
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.vibecapandroid.coms.*
import com.example.vibecapandroid.databinding.ActivityHomeCapturedBinding
import com.google.android.youtube.player.YouTubeIntents.createPlayVideoIntent
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HomeCapturedActivity : AppCompatActivity() {
    var vibe_id : Long?= null
    var video_id:String? = null
    var imagebitmap:Bitmap? = null
    var youtube_link: String? = null
     var realUri:Uri?=null

    private val viewBinding: ActivityHomeCapturedBinding by lazy{
        ActivityHomeCapturedBinding.inflate(layoutInflater)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val view = viewBinding.root
        setContentView(view)

        //button youtube GONE
        viewBinding.btYoutube.visibility= View.GONE

        //progress bar start
        showProgressbar(true)

        //유튜브 출력
        Youtubeplay()

        //button setonClickListener
        //play on youtube button
        viewBinding.btYoutube.setOnClickListener{
            var intent: Intent = createPlayVideoIntent(this,video_id )
            startActivity( intent );
        }

        //home button
        viewBinding.btHome.setOnClickListener{
            val nextIntent = Intent(this, MainActivity::class.java)
            val fragcode=intent.extras?.getInt("frag_code")
            nextIntent.putExtra("frag_code", fragcode)
            startActivity(nextIntent)
            super.finish()
        }
        //share button
        viewBinding.btShare.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, youtube_link)
            }
            startActivity(Intent.createChooser(intent, youtube_link))

        }
        //post button
        viewBinding.btWrite.setOnClickListener{
            if(vibe_id!=null){
                val nextIntent = Intent(this, CommonPostActivity::class.java)
                //nextIntent.putExtra("video_id",video_id)
                nextIntent.putExtra("vibe_id", vibe_id!!.toInt())

                startActivity(nextIntent)
            }
            else{
                Toast.makeText(applicationContext, "서버에 저장되어있는 사진이 없습니다", Toast.LENGTH_SHORT).show();
            }
        }

        //delete button
        viewBinding.btDelete.setOnClickListener{
            deletePhoto()
        }


    }


    override fun onRestart() {
        super.onRestart()
        youtubefragmentshow()
    }

    override fun onBackPressed() {
        val nextIntent = Intent(this, MainActivity::class.java)
        val fragcode=intent.extras?.getInt("frag_code")
        nextIntent.putExtra("frag_code", fragcode)
        startActivity(nextIntent)
        super.finish()
    }

    private fun youtubefragmentshow(){
        var YoutubePlayerFragment = YoutubePlayerFragment.newInstance()
        var bundle = Bundle()
        bundle.putString("VIDEO_ID", video_id)
        YoutubePlayerFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_captured_you_tube_player_view, YoutubePlayerFragment)
            .commitAllowingStateLoss()
        //progresss bar 종료
//         runOnUiThread {
//            showProgressbar(false)
//            viewBinding.btYoutube.visibility = View.VISIBLE
//        }
    }


    private fun showProgressbar(isShow: Boolean){
        if(isShow) viewBinding.progressBar.visibility = View.VISIBLE
        else {
            viewBinding.progressBar.visibility = View.GONE
            viewBinding.btDelete.visibility=View.VISIBLE
            viewBinding.btYoutube.visibility= View.VISIBLE
            viewBinding.btShare.visibility=View.VISIBLE
            viewBinding.btWrite.visibility =View.VISIBLE

        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun timing():String{
        var result :String
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM")
        val formatted = current.format(formatter)
        Log.d("파일",formatted.toString())
        when(formatted.toInt()) {
            in 1..2 -> result = "겨울 "
            12 -> result = "겨울"
            in 3..5 -> result = "봄 "
            in 6..8 -> result = "여름 "
            in 9..11 -> result = "가을 "
            else -> result = ""
            //아 자고 싶다 ㅅㅂ
        }
        val formatter2 = DateTimeFormatter.ofPattern("HH")
        val formatted2 = current.format(formatter2)


        when(formatted2.toInt()){
            in 6..12 -> result = result + "아침"
            in 12..14 -> result = result +"낮"
            in 14..18 -> result = result + "오후"
            in 18 .. 21 -> result = result +"저녁"
            in 21.. 24-> result = result +"밤"
            in 0.. 2-> result = result +"밤"
            in 2..6 -> result = result +"새벽"
            else -> result = result +""
        }
        Log.d("resulttime",result)
        return result
    }


//    // 절대경로 변환 함수 이젠 안쓰지만 혹시모르니 남김
//    private fun absolutelyPath(path: Uri?, context : Context): String {
//        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
//        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
//        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        c?.moveToFirst()
//        var result = c?.getString(index!!)
//        return result!!
//    }

  /*  private fun downloadPhoto(){
        //imagebitmap = intent.getParcelableExtra<Bitmap>("imagebitmap")
        saveFile(RandomFileName(), "image/jpeg", imagebitmap!!) // 휴대폰 local db 에 저장
        Log.d("파일", "파일저장 완료")
        Toast.makeText(applicationContext, "사진 다운로드 성공", Toast.LENGTH_SHORT).show();
    }*/

    private fun deletePhoto(){
        //base url 설정
        val apiService = retrofit.create(HomeApiInterface::class.java)
        apiService.deletephoto(
            userToken, vibe_id

        ).enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
                val responseData = response.body()
                Log.d(
                    "deletephoto",
                    "deletephoto\n"+
                            "isSuccess:${responseData?.is_success}\n " +
                            "Code: ${responseData?.code} \n" +
                            "Message:${responseData?.message} \n" )
                if (responseData?.is_success==true) {
                    when(response.body()?.code){
                        1000 ->{
                            vibe_id =null
                            Log.d("레트로핏",responseData.result)
                            Toast.makeText(applicationContext, "사진 삭제 성공", Toast.LENGTH_SHORT).show();
                        }
                        2100 -> {
                            Log.d ("레트로핏","해당 바이브에 대한 접근 권한이 없습니다" )
                        }
                    }
                }
                else {
                    Toast.makeText(applicationContext, "해당 사진이 이미 서버에서 삭제 되었습니다", Toast.LENGTH_SHORT).show()
                    Log.d("레트로핏","Response Not Success ${response.code()}")
                }

            }
            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                Log.d("레트로핏","레트로핏 호출 실패" +t.message.toString())
            }
        })

    }
    /*// 파일명을 날짜로 함수
    fun RandomFileName(): String {
        val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
        return fileName
    }*/


    /*//사진 저장 함수
    private fun saveFile(filename:String, mimeType:String, bitmap: Bitmap): Uri? {

        var CV = ContentValues()

        // MediaStore에 파일명, mimeType을 지정
        CV.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        CV.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        // 안정성 검사
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            CV.put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        // MediaStore에 파일을 저장
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CV)

        if(uri != null){
            var scriptor = contentResolver.openFileDescriptor(uri, "w")
            val fos = FileOutputStream(scriptor?.fileDescriptor)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                CV.clear()
                // IS_PENDING을 초기화
                CV.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(uri, CV,null, null)
            }
        }
        return uri
    }*/
    //원본 이미지를 불러오는 메소드
    fun loadBitmap(photoUri:Uri):Bitmap?{
        var image:Bitmap?=null
        try{
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O_MR1){
                val source= ImageDecoder.createSource(contentResolver,photoUri)
                image=  ImageDecoder.decodeBitmap(source)
            }else{
                image= MediaStore.Images.Media.getBitmap(contentResolver,photoUri)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        Log.d("imageload","$image")
        return image
    }



    //유튜브 출력
    @RequiresApi(Build.VERSION_CODES.O)
    private fun Youtubeplay(){
        realUri=intent.getParcelableExtra("image_uri")
        Log.d("RealURI","$realUri")
        imagebitmap=loadBitmap(realUri!!)

        Log.d("imageBitMAP","$imagebitmap")
        val fileName = "VibeCap_Photo" + ".jpg"
        Log.d("imagebitmap",imagebitmap.toString())
        val stream = ByteArrayOutputStream()
        imagebitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)

        val byteArray = stream.toByteArray()
        val body = RequestBody.create(MediaType.parse("image/*"), byteArray, 0, byteArray.size)
        var image: MultipartBody.Part = MultipartBody.Part.createFormData("image_file", fileName ,body)
        Log.d("파일",image.toString())
        var time: String = timing() //계절 및 시간
        Log.d("키워드", time+ " "+ feeling)

        //base url 설정
        val apiService = retrofit.create(HomeApiInterface::class.java)
        Log.d("따음표",time +" "+ feeling)
        apiService.postCapture(
            userToken, MEMBER_ID, time +" "+ feeling, // 계절 +시간 날씨 기분 -> api 통해서 가져와야함
            image
        )
            .enqueue(object : Callback<CaptureResponse> {
                override fun onResponse(call: Call<CaptureResponse>, response: Response<CaptureResponse>) {
                    val responseData = response.body()
                    Log.d(
                        "postCapture",
                        "postCapture\n"+
                                "isSuccess:${responseData?.is_success}\n " +
                                "Code: ${responseData?.code} \n" +
                                "Message:${responseData?.message} \n" )
                    if (responseData?.is_success==true) {
                        when(response.body()?.code){
                            1000 ->{
                                Toast.makeText(getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_SHORT).show();
                                Log.d("레트로핏","이미지 전송 성공")
                                video_id = response.body()!!.result.video_id
                                vibe_id = response.body()!!.result.vibe_id
                                youtube_link = response.body()!!.result.youtube_link
                                Log.d("레트로핏",video_id+" "+ youtube_link)
                                var YoutubePlayerFragment = YoutubePlayerFragment.newInstance()
                                var bundle = Bundle()
                                bundle.putString("VIDEO_ID", video_id)
                                YoutubePlayerFragment.arguments = bundle
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.home_captured_you_tube_player_view, YoutubePlayerFragment)
                                    .commitNow()
                                //progresss bar 종료
                                runOnUiThread {
                                    showProgressbar(false)
                                    viewBinding.btYoutube.visibility = View.VISIBLE
                                }
                            }

                            3500 -> {
                                Log.d ("레트로핏","이미지 전송 실패 -> 사진 용량 제한 : 7MB" )
                            }
                            3502 -> {
                                Log.d("레트로핏", "외부 API 호출 실패 -> youtube,vision api 관련 에러 ")
                            }
                        }

                    } else {
                        Log.d("레트로핏",response.body().toString())
                        Toast.makeText(getApplicationContext(), "예기치 못한 오류가 일어났습니다..", Toast.LENGTH_LONG).show();
                        Log.d("레트로핏","Response Not Success ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<CaptureResponse>, t: Throwable) {
                    Log.d("레트로핏","레트로핏 호출 실패" +t.message.toString())
                }
            })
    }



}