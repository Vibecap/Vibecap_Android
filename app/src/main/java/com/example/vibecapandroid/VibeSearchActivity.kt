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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.PostContentData
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
        setContentView(viewBinding.root)


        // 툴바
        val toolbar = findViewById<Toolbar>(R.id.toolBar_top)
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //searchAPI()
        viewBinding.etSearchTag.setOnClickListener {
            Toast.makeText(applicationContext, "search success", Toast.LENGTH_SHORT).show()
            val tagname = viewBinding.etSearchTag.toString()
            // 검색 API
            searchAPI("신나는")
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

    private fun searchAPI(tagname:String) {
        searchRetrofitObject.getApiService().postAllCheck(tagname).enqueue(object :
            Callback<PostTagResponse> {
            // api 호출 성공시
            override fun onResponse(call: Call<PostTagResponse>, response: Response<PostTagResponse>) {
                if (response.isSuccessful){
                    val respData = response.body()
                    val respDataResult = respData?.result?.content
                    if (respData != null){
                        Log.d(
                            "searchResult",
                            "searchResult\n"+
                                    "isSuccess:${respData?.is_success}\n " +
                                    "Code: ${respData?.code} \n" +
                                    "Message:${respData?.message} \n"
                        )
                        if(respData.is_success){
                            when(respData?.code){
                                1000-> {
                                    Log.d("tagSearchResult",
                                    "result : ${respDataResult}\n")

                                    //데이터 저장하기
                                    SearchSaveData(tagname,respDataResult)
                                }
                                3011 -> {
                                    Toast.makeText(applicationContext,"해당 태그를 가진 게시물이 없습니다.", Toast.LENGTH_SHORT).show()

                                }
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

    private fun SearchSaveData(tagname: String, respData: List<PostContentData>?) {
        if (tagname.isNullOrEmpty()){
            Log.d("tagEmpty","태그 이름 없음")
        }else{
            val vibeSearchAdapterClass = VibeSearchAdapterClass(respData as ArrayList<PostContentData>)
            val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            viewBinding.vibeSearchMainRv.layoutManager = layoutManager
            viewBinding.vibeSearchMainRv.adapter = vibeSearchAdapterClass

            vibeSearchAdapterClass.setMyItemClickListener(object :
                VibeSearchAdapterClass.MyItemClickListener{
                override fun onItemClick(postContentData: PostContentData) {
                    //val intent = Intent(context, VibePostActivity::class.java)
                    //intent.putExtra("post_id",postContentData.post_id)
                }
                }
            )
        }
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