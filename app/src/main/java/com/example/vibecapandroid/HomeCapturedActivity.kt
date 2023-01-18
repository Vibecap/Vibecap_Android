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
import com.example.vibecapandroid.databinding.ActivityRegisterEmailBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import kotlin.io.path.Path


class HomeCapturedActivity : AppCompatActivity() {


    private val viewBinding: ActivityHomeCapturedBinding by lazy{
        ActivityHomeCapturedBinding.inflate(layoutInflater)
    }

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = viewBinding.root
        setContentView(view)
        val imageString = intent.getStringExtra("uri")
        val uri: Uri = Uri.parse(imageString)
        viewBinding.imageView5.setImageURI(uri)
        val file = File(absolutelyPath(uri,this))
        var requestBody: RequestBody = RequestBody.create(MediaType.parse("image/jpeg"), file)
        var body: MultipartBody.Part = MultipartBody.Part.createFormData("image_file", file.name, requestBody)
        Log.d("suhoon" , body.toString())


        




        //이 부분부터 Retrofit 실습하는것
        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //base url 설정
        val apiService = retrofit.create(VibeApiInterface::class.java)
        apiService.VibeCapture(
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2JhazQyMDBuaW1AbmF0ZS5jb20iLCJhdWQiOiJ2aWJlY2FwRGVmYXVsdFNlcnZlciIsImlhdCI6MTY3NDAyNjY4MSwiZXhwIjoxNjc0MDMwMjgxLCJyb2xlIjoiUk9MRV9NRU1CRVIifQ.f0c8XC_iqt-Buue-yFHZF0tSC5MtAKyEY9ERQuoZmYc",
            "multipart/form-data",
            2, extra_info("화창한","2023-01-18","신나는"),
            body
        )
            .enqueue(object : Callback<CaptureResponse> {
                override fun onResponse(call: Call<CaptureResponse>, response: Response<CaptureResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
                        Log.d("레트로핏","이미지 전송 성공"+response.body())
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                        Log.d("레트로핏","Response Not Success ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<CaptureResponse>, t: Throwable) {
                    Log.d("레트로핏","이미지 전송 실패 " +t.message.toString())
                }

            })

        // 유튜브 플레이어 뷰
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.you_tube_player_view, YoutubePlayerFragment.newInstance())
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