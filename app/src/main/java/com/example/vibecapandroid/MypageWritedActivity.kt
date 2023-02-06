package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart

class MypageWritedActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    //arrayList 선언
    private var arrayList:ArrayList<CheckMypageWritedResponseResult> ? = null
    private var mypageWritedAdapters:MypageWritedadaptersClass ? = null



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_writed)

        recyclerView = findViewById(R.id.recyclerview)
        gridLayoutManager = GridLayoutManager(applicationContext,3,
            LinearLayoutManager.VERTICAL,false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        //arrayList 초기화
        arrayList = ArrayList()
        mypageWritedAdapters = MypageWritedadaptersClass(applicationContext,arrayList!!)
        recyclerView?.adapter = mypageWritedAdapters


        val mypage_back = findViewById<ImageView>(R.id.activity_mypage_writed_back)
        mypage_back.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MypageProfileActivity::class.java)
            startActivity(intent)
            this@MypageWritedActivity.finish()
        })

        //웹 브라우저 창 열기
        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //어떤 주소로 들어갈지 입력
        val apiService = retrofit.create(MypageApiInterface::class.java)

        //입력한 주소중 하나로 연결 시도
        apiService.getWritedCheck(userToken, MEMBER_ID).enqueue(object :
            Callback<CheckMypageWritedResponse> {
            @SuppressLint("ResourceType")
            override fun onResponse(
                call: Call<CheckMypageWritedResponse>,
                response: Response<CheckMypageWritedResponse>
            ) {

                if (response.isSuccessful) {
                    val responseData = response.body()

                    if (responseData !== null) {

                        Log.d(
                            "Retrofit",
                            "MypageResponse\n"+
                                    "isSuccess:${responseData.is_success}" +
                                    "Code:${responseData.code}"+
                                    "Message:${responseData.message}"+
                                    "Result:${responseData.result}"
                        )
                        if(responseData.is_success){
                            Log.d("responseData","${responseData.result}")
                            if(responseData.result.isEmpty()){
                                Log.d("게시된 사진 없음","게시된 사진 없음")
                            }
                            else{

                                //if(responseData.result[i].isEmpty())
                                //arrayList?.add(MypageWritedimageClass((responseData.result[0].vibe_image)))
                                arrayList!!.addAll(responseData.result.toMutableList())
                                Log.d("ArrayList is success","${arrayList}")
                                mypageWritedAdapters!!.notifyDataSetChanged()
                            }
                        }

                        //var writed_image = findViewById<ImageView>(R.layout.activity_mypage_writedgrid)
                        //writed_image!!.setImageBitmap(stringToBitmap(responseData.result.vibe_image))

                        //arrayList?.add(MypageWritedimageClass(stringToBitmap(responseData.result[0].vibe_image)))
                        //arrayList 추가
                        //arrayList?.add(MypageWritedimageClass((responseData.result[0].vibe_image)))




                        //mypageWritedAdapters!!.notifyDataSetChanged()
                    }
                    else{
                        Log.d("Retrofit","Null data") }

                } else {
                    Log.w("Retrofit", "Response Not Successful${response.code()}")
                }
            }

            override fun onFailure(call: Call<CheckMypageWritedResponse>, t: Throwable) {
                Log.e("Retrofit","Error",t)
            }

        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MypageProfileActivity::class.java)
        startActivity(intent)
        this@MypageWritedActivity.finish()
    }

}