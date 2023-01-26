package com.example.vibecapandroid
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.vibecapandroid.coms.*
import com.example.vibecapandroid.databinding.ActivityHomeCapturedBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.Math.abs


class HomeCapturedActivity : AppCompatActivity() {

    var video_id:String? = null

    private val viewBinding: ActivityHomeCapturedBinding by lazy{
        ActivityHomeCapturedBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        //좌표 이용해서 날씨 가져오기
        //getWeather(latitude, longitude)
        super.onCreate(savedInstanceState)
        val view = viewBinding.root
        setContentView(view)

        //유튜브 출력
        Youtubeplay()
    }



    // 절대경로 변환 함수
    private fun absolutelyPath(path: Uri?, context : Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        var result = c?.getString(index!!)
        return result!!
    }
    //유튜브 출력
    private fun Youtubeplay(){
        //uri intent로 가져오기
        //val imageString = intent.getStringExtra("uri")
        //val uri: Uri = Uri.parse(imageString)
        //viewBinding.imageView5.setImageURI(imageuri)
        //uri를 절대경로로 만들고, request body로 만들어주기
        val file = File(absolutelyPath(imageuri,this))
        Log.d("파일",file.toString())
        var requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        var image: MultipartBody.Part = MultipartBody.Part.createFormData("image_file", file.name, requestBody)
        Log.d("파일",image.toString())
        //jwt 가져오기
        var jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzMmhvb25AbmF2ZXIuY29tIiwiYXVkIjoidmliZWNhcERlZmF1bHRTZXJ2ZXIiLCJpYXQiOjE2NzQ2NTMyNDQsImV4cCI6MTY3OTgzNzI0NCwicm9sZSI6IlJPTEVfTUVNQkVSIn0.zebkIjMpIz2I_AMW17gfSVU-Cs1VMklRalupe_jgEVI"
        //Gson create
        var gson: Gson = GsonBuilder()
            .setLenient()
            .create()
        //Retrofit build
        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        //base url 설정
        val apiService = retrofit.create(HomeApiInterface::class.java)
        apiService.HomeCapture(
            jwt,
            3, "화창한 아침 신나는", // 계절 +시간 날씨 기분 -> api 통해서 가져와야함
            image
        )
            .enqueue(object : Callback<CaptureResponse> {
                override fun onResponse(call: Call<CaptureResponse>, response: Response<CaptureResponse>) {
                    val responseData = response.body()
                    Log.d(
                        "getHistoryAllResponse",
                        "getHistoryAllResponse\n"+
                                "isSuccess:${responseData?.is_success}\n " +
                                "Code: ${responseData?.code} \n" +
                                "Message:${responseData?.message} \n" )
                    if (responseData?.is_success==true) {
                        when(response.body()?.code){
                            1000 ->{
                                Log.d("레트로핏","요청 성공")
                                Toast.makeText(getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
                                Log.d("레트로핏","이미지 전송 성공"+" " + response.body())
                                video_id = response.body()!!.result.video_id
                                Log.d("레트로핏",video_id.toString())
                                var YoutubePlayerFragment = YoutubePlayerFragment.newInstance()
                                var bundle = Bundle()
                                bundle.putString("VIDEO_ID", video_id)
                                YoutubePlayerFragment.arguments = bundle
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.you_tube_player_view, YoutubePlayerFragment)
                                    .commitNow()
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