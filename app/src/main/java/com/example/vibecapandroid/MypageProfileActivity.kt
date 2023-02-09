package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64.NO_WRAP
import android.util.Base64.decode
import android.util.Log
import android.view.View
import android.widget.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Base64.*
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.*
import okhttp3.MultipartBody

class MypageProfileActivity : AppCompatActivity() {

    var mypageList = arrayListOf<MypageProfileClass>(
        MypageProfileClass(R.drawable.ic_activity_mypage_profile_act,"활동 내역",R.drawable.ic_activity_mypage_profile_next),
        MypageProfileClass(R.drawable.ic_activity_mypage_profile_writed,"내 게시물",R.drawable.ic_activity_mypage_profile_next),
        MypageProfileClass(R.drawable.ic_activity_mypage_profile_like,"좋아요 한 게시물",R.drawable.ic_activity_mypage_profile_next),
        MypageProfileClass(R.drawable.ic_activity_mypage_profile_scrap,"스크랩 한 게시물",R.drawable.ic_activity_mypage_profile_next),
        MypageProfileClass(R.drawable.ic_activity_mypage_profile_setting,"설정",R.drawable.ic_activity_mypage_profile_next)

    )



    private val apiService = retrofit.create(MypageApiInterface::class.java)



    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_profilelist)



        val Adater = MypageProfileadaptersClass(this,mypageList)
        val activity_mypage_profilelist = findViewById<ListView>(R.id.activity_mypage_profilelist)
        activity_mypage_profilelist.adapter = Adater

        activity_mypage_profilelist.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if(position==0){
                val selectItem = parent.getItemAtPosition(position) as MypageProfileClass
                val intent = Intent(this, MypageAlarmActivity::class.java)
                intent.putExtra("활동내역",mypageList[position].profile_textview)
                startActivity(intent)}
            else if(position==1){
                val selectItem = parent.getItemAtPosition(position) as MypageProfileClass
                val intent = Intent(this,MypageWritedActivity::class.java)
                intent.putExtra("내 게시물",mypageList[position].profile_textview)
                startActivity(intent)}
            else if(position==2){
                val selectItem = parent.getItemAtPosition(position) as MypageProfileClass
                val intent = Intent(this,MypageLikeActivity::class.java)
                intent.putExtra("좋아요 한 게시물",mypageList[position].profile_textview)
                startActivity(intent)}
            else if(position==3){
                val selectItem = parent.getItemAtPosition(position) as MypageProfileClass
                val intent = Intent(this,MypageScrapActivity::class.java)
                intent.putExtra("스크랩 한 게시물",mypageList[position].profile_textview)
                startActivity(intent)}
            else if(position==4){
                val selectItem = parent.getItemAtPosition(position) as MypageProfileClass
                val intent = Intent(this,MypageSetupActivity::class.java)
                intent.putExtra("설정",mypageList[position].profile_textview)
                startActivity(intent)}
        }

        val mypage_back = findViewById<ImageView>(R.id.activity_mypage_nickname_close)
        mypage_back.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this@MypageProfileActivity.finish()
        })






        Log.d("userToken","$userToken")
        //입력한 주소중 하나로 연결 시도
        apiService.getMypageCheck(userToken, MEMBER_ID).enqueue(object :Callback<CheckMypageResponse> {
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
                                    "Result:${responseData.result.email}"+
                                    "Result:${responseData.result.profile_image}"+
                                    "Result:${responseData.result.nickname}"
                        )
                        var nickname = findViewById<TextView>(R.id.activity_mypage_profilelist_nickname)
                        nickname.setText(responseData.result.nickname)
                        var email = findViewById<TextView>(R.id.activity_mypage_profilelist_email)
                        email.setText(responseData.result.email)
                            Glide.with(this@MypageProfileActivity)
                                .load(responseData.result.profile_image)
                                .into(findViewById(R.id.activity_mypage_profilelist_profileimg))
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



/*

        apiService.patchMypageImgChange(userToken, patchMypageImgInput(1,"")).enqueue(object :Callback<patchMypageImgResponse> {
            override fun onResponse(
                call: Call<patchMypageImgResponse>,
                response: Response<patchMypageImgResponse>
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


                        var profile_image = findViewById<ImageView>(R.id.activity_mypage_profilelist_profileimg)
                        //profile_image.setImageBitmap(responseData.result.profile_image)
                        profile_image!!.setImageBitmap(stringToBitmap(responseData.result))
                    }
                    else{
                        Log.d("Retrofit","Null data") }

                } else {
                    Log.w("Retrofit", "Response Not Successful${response.code()}")
                }
            }

            override fun onFailure(call: Call<patchMypageImgResponse>, t: Throwable) {
                Log.e("Retrofit","Error",t)
            }

        })


*/
/*

        apiService.patchProfileimgChange(userToken,"VibecapImg").enqueue(object :Callback<CheckMypageResponse> {
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

*/



    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this@MypageProfileActivity.finish()
    }

    override fun onRestart() {
        super.onRestart()
        apiService.getMypageCheck(userToken, MEMBER_ID).enqueue(object :Callback<CheckMypageResponse> {
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
                                    "Result:${responseData.result.email}"+
                                    "Result:${responseData.result.profile_image}"+
                                    "Result:${responseData.result.nickname}"
                        )
                        var nickname = findViewById<TextView>(R.id.activity_mypage_profilelist_nickname)
                        nickname.setText(responseData.result.nickname)
                        var email = findViewById<TextView>(R.id.activity_mypage_profilelist_email)
                        email.setText(responseData.result.email)
                        Glide.with(this@MypageProfileActivity).load(responseData.result.profile_image).into(findViewById(R.id.activity_mypage_profilelist_profileimg))
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





}




