package com.example.vibecapandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.vibecapandroid.coms.PostContentData
import com.example.vibecapandroid.coms.PostTagResponse
import com.example.vibecapandroid.coms.VibePostTagInterface
import com.example.vibecapandroid.databinding.ActivityVibeDetailBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class VibeDetailActivity : AppCompatActivity() {
    val viewBinding : ActivityVibeDetailBinding by lazy {
        ActivityVibeDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolBar_top)
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 데이터 받기
        val tagNameIntent = intent.getStringExtra("tagname")
        viewBinding.tvLogo.text = "#$tagNameIntent"
        Log.d("tagnameIntent", tagNameIntent.toString())

        // 게시물 작성 열기
        viewBinding.btnAddpostdetail.setOnClickListener {
            val intent = Intent(this, HistoryPostActivity::class.java)
            startActivity(intent)
        }

        // api 호출
        callTagDetail(tagNameIntent.toString())
    }
    
    // api
    object DetailTagRetrofitObject{
        private fun getRetrofit(): Retrofit{
            return Retrofit.Builder()
                .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        fun getApiService(): VibePostTagInterface{
            return getRetrofit().create(VibePostTagInterface::class.java)
        }
    }

    // 태그별 api
    private fun callTagDetail(tagname:String){
        DetailTagRetrofitObject.getApiService().postAllCheck(tagname).enqueue(object :
        Callback<PostTagResponse>{
            // api 호출 성공시
            override fun onResponse(
                call: Call<PostTagResponse>,
                response: Response<PostTagResponse>
            ) {
                if(response.isSuccessful){
                    val respData = response.body()
                    val respDataResult = respData?.result?.content
                    if(respData != null){
                        Log.d(
                            "TagDatailResult",
                            "TagDatailResult\n" +
                                    "isSuccess:${respData?.is_success}\n " +
                                    "Code: ${respData?.code} \n" +
                                    "Message:${respData?.message} \n"
                        )
                        if(respData.is_success){
                            when(respData?.code){
                                1000 -> {
                                    Log.d("tagDatailResult",
                                        "result : ${respDataResult}\n"
                                        )
                                    //데이터 저장하기
                                    DetailTagsaveData(tagname, respDataResult)
                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<PostTagResponse>, t: Throwable) {
                Log.e("retrofit onFailure_Detail", "${t.message.toString()}")
            }
        })
    }

    private fun DetailTagsaveData(tagname: String, respData: List<PostContentData>?) {
        if(tagname.isNullOrEmpty()){
            Log.d("tagEmpty","태그 이름 없음")
        }else{
            val vibeDatailAdapterClass = VibeDetailAdapterClass(respData as ArrayList<PostContentData>)
            val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            viewBinding.vibeDetailPostsRv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
            viewBinding.vibeDetailPostsRv.adapter = vibeDatailAdapterClass

            vibeDatailAdapterClass.setMyItemClickListener(object :
                VibeDetailAdapterClass.MyItemClickListener{
                override fun onItemClick(postContentData: PostContentData) {
                    //val intent = Intent(context, VibePostActivity::class.java)
                    //intent.putExtra("post_id",postContentData.post_id)
                }
                })

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