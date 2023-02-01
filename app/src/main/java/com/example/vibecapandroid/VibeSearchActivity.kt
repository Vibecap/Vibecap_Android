package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.PostTagResponse
import com.example.vibecapandroid.coms.VibePostApiInterface
import com.example.vibecapandroid.coms.VibePostTagInterface
import com.example.vibecapandroid.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class VibeSearchActivity : AppCompatActivity() {
    val viewBinding : ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        // 툴바
        val toolbar = findViewById<Toolbar>(R.id.toolBar_top)
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //searchAPI()
        findViewById<ImageButton>(R.id.imageButton_tagsearch11).setOnClickListener {
            Toast.makeText(applicationContext, "search success", Toast.LENGTH_SHORT).show()

            // 검색 API 추가
            searchAPI()
            // 검색 결과 출력
        }

    }

    // api
    object searchRetrofitObject {
        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getApiService(): VibePostTagInterface {
            return getRetrofit().create(VibePostTagInterface::class.java)
        }
    }

    private fun searchAPI() {
        val tagName = findViewById<EditText>(R.id.et_search_tag).toString()
        tagName == "신나는 "
        searchRetrofitObject.getApiService().postAllCheck(tagName).enqueue(object :
            Callback<PostTagResponse> {
            // api 호출 성공시
            override fun onResponse(call: Call<PostTagResponse>, response: Response<PostTagResponse>) {
                if (response.isSuccessful){
                    val responseData = response.body()
                    if (responseData != null){
                        //Toast.makeText(applicationContext, "success", Toast.LENGTH_SHORT).show()
                        Log.d(
                            "searchResult",
                            "searchResult\n"+
                                    "isSuccess:${responseData?.is_success}\n " +
                                    "Code: ${responseData?.code} \n" +
                                    "Message:${responseData?.message} \n"
                        )
                        if(responseData.is_success){
                            //val editor = getSharedPreferences(
                                //"sharecropped",
                                //MODE_PRIVATE
                            //).edit()
                            //editor.putInt("post_id",responseData.result)
                            //editor.putInt("member_id",responseData.result[0].content[0].member_id)
                            //editor.putInt("vibe_id",responseData.result[0].content[0].vibe_id)
                            //editor.putString("vibe_image",responseData.result[1].vibe_image)
                            //editor.apply()
                            //stringToBitmap(editor.putString("vibe_image",responseData.result[1].vibe_image).toString())
                            val imageView = viewBinding.imageViewMain1
                            val defaultImage = R.drawable.image_ic_activity_history_album_list1
                            //val url = editor.putString("vibe_image",responseData.result[0].content[0].vibe_image)
                            val url = "https://firebasestorage.googleapis.com/v0/b/vibecap-ee692.appspot.com/o/b9bf7d74-88f3-4b06-952b-dc9c59f8090ajpg?alt=media"
                            Glide.with(this@VibeSearchActivity)
                                .load(url) // 불러올 이미지 url
                                .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                                .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                                .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                                .into(imageView) // 이미지를 넣을 뷰
                        }else{
                            if(responseData.code == 3011){
                                // xml에 tvView 추가해서 문구 띄우기
                                Toast.makeText(applicationContext,"해당 태그를 가진 게시물이 없습니다.", Toast.LENGTH_SHORT).show()

                            }
                        }
                    }
                }
            }

            // api 호출 실패시
            override fun onFailure(call: Call<PostTagResponse>, t: Throwable) {
                Log.e("retrofit Search onFailure2", "${t.message.toString()}")
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> {}
        }

        return super.onOptionsItemSelected(item)
    }
}