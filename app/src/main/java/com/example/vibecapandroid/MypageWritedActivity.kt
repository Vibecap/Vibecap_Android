package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vibecapandroid.coms.CheckMypageResponse
import com.example.vibecapandroid.coms.MypageApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MypageWritedActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var arrayList:ArrayList<MypageWritedimageClass> ? = null
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
        arrayList = ArrayList()
        arrayList = setDataInList()
        mypageWritedAdapters = MypageWritedadaptersClass(applicationContext,arrayList!!)
        recyclerView?.adapter = mypageWritedAdapters


        val mypage_back = findViewById<ImageView>(R.id.activity_mypage_writed_back)
        mypage_back.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MypageProfileActivity::class.java)
            startActivity(intent)
        })

        //웹 브라우저 창 열기
        val retrofit = Retrofit.Builder()
            .baseUrl("http://175.41.230.93:8080/app/my-page/posts")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //어떤 주소로 들어갈지 입력
        val apiService = retrofit.create(MypageApiInterface::class.java)

        //입력한 주소중 하나로 연결 시도
        apiService.getWritedCheck(1).enqueue(object :
            Callback<CheckMypageResponse> {
            override fun onResponse(
                call: Call<CheckMypageResponse>,
                response: Response<CheckMypageResponse>
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
                    }
                    else{
                        Log.d("Retrofit","Null data") }

                } else {
                    Log.w("Retrofit", "Response Not Successful${response.code()}")
                }
            }

            override fun onFailure(call: Call<CheckMypageResponse>, t: Throwable) {
                Log.e("Retrofit","Error",t)
            }

        })

    }

    private fun setDataInList(): ArrayList<MypageWritedimageClass>{
        var items: ArrayList<MypageWritedimageClass> = ArrayList()

        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list3))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(MypageWritedimageClass(R.drawable.image_ic_activity_history_album_list3))




        return items
    }
}