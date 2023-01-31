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
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vibecapandroid.coms.CheckMypageLikeResponse
import com.example.vibecapandroid.coms.CheckMypageLikeResponseResult
import com.example.vibecapandroid.coms.CheckMypageWritedResponse
import com.example.vibecapandroid.coms.MypageApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MypageLikeActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var arrayList:ArrayList<CheckMypageLikeResponseResult> ? = null
    private var mypageLikeAdapters:MypageLikeadaptersClass ? = null

    private fun stringToBitmap(encodedString: String?): Bitmap {
        val imageBytes = Base64.decode(encodedString, Base64.NO_WRAP)
        return BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_like)

        recyclerView = findViewById(R.id.recyclerview)
        gridLayoutManager = GridLayoutManager(applicationContext,3,
            LinearLayoutManager.VERTICAL,false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        arrayList = ArrayList()
        mypageLikeAdapters = MypageLikeadaptersClass(applicationContext,arrayList!!)
        recyclerView?.adapter = mypageLikeAdapters





        val mypage_back = findViewById<ImageView>(R.id.activity_mypage_like_back)
        mypage_back.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MypageProfileActivity::class.java)
            startActivity(intent)
        })

        //안됨 (하나하나 클릭했을때 그에 따른 이미지가 보이도록 수정)
     /*   val album_list = findViewById<ImageView>(R.id.activity_mypage_album_image)
        album_list.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,MypagePostActivity::class.java)
            startActivity(intent)
        })*/

        //웹 브라우저 창 열기
        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //어떤 주소로 들어갈지 입력
        val apiService = retrofit.create(MypageApiInterface::class.java)

        //입력한 주소중 하나로 연결 시도
        apiService.getLikeCheck(userToken, MEMBER_ID).enqueue(object :
            Callback<CheckMypageLikeResponse> {
            @SuppressLint("ResourceType")
            override fun onResponse(
                call: Call<CheckMypageLikeResponse>,
                response: Response<CheckMypageLikeResponse>
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
                            if(responseData.result.isEmpty()){
                                Log.d("좋아요한 사진 없음","좋아요한 사진 없음")
                            }
                            else{
                                arrayList!!.addAll(responseData.result.toMutableList())
                                //arrayList?.add(MypageLikeimageClass((responseData.result[0].vibe_image)))
                                Log.d("ArrayList is success","${arrayList}")
                                mypageLikeAdapters!!.notifyDataSetChanged()
                            }
                        }


                    }
                    else{
                        Log.d("Retrofit","Null data") }

                } else {
                    Log.w("Retrofit", "Response Not Successful${response.code()}")
                }
            }

            override fun onFailure(call: Call<CheckMypageLikeResponse>, t: Throwable) {
                Log.e("Retrofit","Error",t)
            }

        })




    }




}