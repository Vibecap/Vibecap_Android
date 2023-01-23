package com.example.vibecapandroid


import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vibecapandroid.coms.*
import com.example.vibecapandroid.databinding.ActivityHomeCapturedBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File



class HomeCapturedActivity : AppCompatActivity() {

    private val viewBinding: ActivityHomeCapturedBinding by lazy{
        ActivityHomeCapturedBinding.inflate(layoutInflater)
    }

    private var video_id:String = "fd"

    private fun video_id_setter(videoid: String) {
        video_id  = videoid
        return
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = viewBinding.root
        setContentView(view)
        //uri intent로 가져오기
        val imageString = intent.getStringExtra("uri")
        val uri: Uri = Uri.parse(imageString)
        viewBinding.imageView5.setImageURI(uri)
        //uri를 절대경로로 만들고, request body로 만들어주기
        val file = File(absolutelyPath(uri,this))
        var requestBody: RequestBody = RequestBody.create(MediaType.parse("image/jpeg"), file)
        var image: MultipartBody.Part = MultipartBody.Part.createFormData("image_file", file.name, requestBody)

        //jwt 가져오기
        var jwt = " "


        //Retrofit build
        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //base url 설정
        val apiService = retrofit.create(VibeApiInterface::class.java)
        apiService.VibeCapture(
            jwt,
            "multipart/form-data",
            2, extra_info("화창한","아침","신나는"), // 계절 시간 날씨 기분 -> api 통해서 가져와야함
            image
        )
            .enqueue(object : Callback<CaptureResponse> {
                override fun onResponse(call: Call<CaptureResponse>, response: Response<CaptureResponse>) {
                    if (response.isSuccessful) {
                        when(response.body()?.code){
                            1000 ->{
                                Log.d("레트로핏","요청 성공")
                                Toast.makeText(getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
                                Log.d("레트로핏","이미지 전송 성공"+" " + response.body())
                                val videoid = response.body()!!.CaptureResult.video_Id
                                video_id_setter(videoid)


                            }


                            3500 -> {
                                Log.d ("레트로핏","이미지 전송 실패 -> 사진 용량 제한 : 7MB" )
                            }



                            3502 -> {
                                Log.d("레트로핏", "외부 API 호출 실패 -> youtube,vision api 관련 에러 ")
                            }


                        }

                        
                    } else {
                        Toast.makeText(getApplicationContext(), "예기치 못한 오류가 일어났습니다..", Toast.LENGTH_LONG).show();
                        Log.d("레트로핏","Response Not Success ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<CaptureResponse>, t: Throwable) {
                    Log.d("레트로핏","레트로핏 호출 실패" +t.message.toString())
                }

            })

        // 유튜브 플레이어 뷰
        if (savedInstanceState == null) {
            var YoutubePlayerFragment = YoutubePlayerFragment.newInstance()
            var bundle = Bundle()
            bundle.putString("VIDEO_ID", video_id)
            YoutubePlayerFragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.you_tube_player_view, YoutubePlayerFragment)
                .commitNow()
        }
    }


    // 절대경로 변환 함수
    fun absolutelyPath(path: Uri?, context : Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        var result = c?.getString(index!!)
        return result!!
    }
}