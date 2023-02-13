package com.example.vibecapandroid



import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.Image
import android.util.Base64
import android.util.Log

import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.*

import com.example.vibecapandroid.databinding.ActivityMainBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime


/*
    1.JDK 설정은 11로 맨 상단에 있는 것으로 선택(안드로이드 스튜디오 오른쪽하단에 event log 보면 jdk 선택창이 뜰꺼임)
    2.모든 Activity 및 Fragment는 ViewBinding으로 통일(R.id.layout방식은 다른 layout의 객체를 선택할 가능성이있음,UMC 3강 참고)
    -fragment에서 recylceview등 특정 상황에는 viewBingding이 사용불가한거같아요(일단 지금까지 제가 알기로는) 이경우는 R.id 방식 사용해요
    -R.id도 어쩔수없이 사용해야하니 꼭 Layout에서 각 요소들 이름 명확히 양식 지켜서 부탁드립니다. 중복되면 찾기 힘들어서요!
    3.build.gradle,settings.gradle 등 gradle Scripts 부분 수정시 단톡에 반드시 말해주세요 merge시 conflict 해결 오래걸릴수있음.
    4.Color랑 아이콘 중복되는거 있는데 이부분은 그냥 중복생각하지말고 각 필요할때마다 추가해서 넣는걸로 하죠.
    -즉,layout의 아이콘과 실제 drawable에 존재하는 xml파일이 실제로 1대1 대응이라 생각하시면 됩니다.
    5.파일 이름이 수정되었을겁니다. 혹시 이게 뭐지 싶은부분은 바로 말씀주세요 ㅎㅎ
     */

/*
        1.먼저 login 여부를 boolean으로 check 한다 main에서 체크하며 sharedref를 사용함->기본값은 false이다.
        2.false라면 Login Activity로 이동한다.
        //하단 부분을 API 사용해야할듯함
        3.회원가입 Activity로 이동후에 회원가입이 완료되면 login 여부 true로 체크 한 다음에 Main으로 이동 or
        3.Login Activity에서 로그인을 하면 login 여부값을 true로 만든다.
        4.Main에서는 뒤로가기 클릭시 앱 탈출
*/




public lateinit var userToken:String
public var arrayList:ArrayList<com.example.vibecapandroid.coms.HistoryMainImageClass>?=null
public var MEMBER_ID:Long=0



val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

class MainActivity : AppCompatActivity() {
    val apiService=retrofit.create(HistoryApiInterface::class.java)
    private var waitTime = 0L
    private val viewBinding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private fun setProfileImage(){

        val resources: Resources = this.resources
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_default_profile_image)
        val fileName = "VibeCap_Photo" + ".jpg"
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        val body = RequestBody.create(MediaType.parse("image/*"), byteArray, 0, byteArray.size)
        val image: MultipartBody.Part = MultipartBody.Part.createFormData("profile_image", fileName ,body)
            val apiService=retrofit.create(MypageApiInterface::class.java)
            apiService.patchMypageImgChange(userToken, MEMBER_ID, image).enqueue(object :
                Callback<patchMypageImgResponse> {
                @SuppressLint("ResourceType")
                override fun onResponse(
                    call: Call<patchMypageImgResponse>,
                    response: Response<patchMypageImgResponse>

                ) {
                    val responseData = response.body()
                    Log.d(
                        "Retrofit",
                        "MypageResponseImgChange\n"+
                                "isSuccess:${responseData?.is_success}" +
                                "Code:${responseData?.code}"+
                                "Message:${responseData?.message}"+
                                "Result:${responseData?.result}"
                    )
                    if (response.isSuccessful) {
                        Log.d("SetProfileImg","Img set Completed")
                    } else {
                        Log.w("Retrofit_notsuccess", "Response Not Successful${response.code()}")
                    }
                }
                override fun onFailure(call: Call<patchMypageImgResponse>, t: Throwable) {
                    Log.e("Retrofitfail","Error",t)
                }
        })
    }
    private fun getProfileImage(){
        val apiService=retrofit.create(MypageApiInterface::class.java)
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
                            "MypageResponseSetProfileData\n"+
                                    "isSuccess:${responseData.is_success}" +
                                    "Code:${responseData.code}"+
                                    "Message:${responseData.message}"+
                                    "Result:${responseData.result.email}"+
                                    "Result:${responseData.result.profile_image}"+
                                    "Result:${responseData.result.nickname}"
                        )
                        if(responseData.result.profile_image==null){
                           setProfileImage()
                        }
                        else {
                        }
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


    fun setDataInList(){
        arrayList = ArrayList()
        apiService.getHistoryAll(userToken, MEMBER_ID)
            .enqueue(object : Callback<HistoryAllResponse> {
                override fun onResponse(call: Call<HistoryAllResponse>, response: Response<HistoryAllResponse>) {
                    val responseData=response.body()
                    if(response.isSuccessful){
                        if (responseData != null) {
                            if(responseData.is_success) {
                                if(responseData.result.album.isEmpty()) {
                                    Log.d("찍은 사진 없음","찍은 사진 없음")
                                }
                                else{
                                    arrayList!!.addAll(responseData.result.album.toMutableList())
                                    historyMainAdapters?.notifyDataSetChanged()
                                }
                            }
                        }
                        else
                        { Log.d("getHistory","getHistoryAll Response Null data") }
                    }
                    else{ Log.d("getHistory","getHistoryAll Response Response Not Success") }
                }
                override fun onFailure(call: Call<HistoryAllResponse>, t: Throwable) { Log.d("getHistory","${t.toString()}") }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_VibecapAndroid)

        // 상태바 설정
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = Color.TRANSPARENT
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

        val windowController = WindowInsetsControllerCompat(this.window, this.window.decorView)
        windowController.isAppearanceLightStatusBars = true
        WindowCompat.setDecorFitsSystemWindows(this.window, true)


        var isLoggedIn=getSharedPreferences("sharedprefs", Context.MODE_PRIVATE).getBoolean("isLoggedIn",false)
        if(!isLoggedIn){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            userToken=getSharedPreferences("sharedprefs",Context.MODE_PRIVATE).getString("Token","none")!!
            MEMBER_ID=getSharedPreferences("sharedprefs",Context.MODE_PRIVATE).getLong("Member_Id",0)!!
            Log.d("Token","$userToken")
            Log.d("Member_ID","${MEMBER_ID}")
            setDataInList()
            getProfileImage()
        }
        setTheme(R.style.Theme_VibecapAndroid)
        setContentView(viewBinding.root)
        supportFragmentManager
            .beginTransaction()
            .replace(viewBinding.containerFragment.id , HomeMainFragment())//activty main의 컨테이너 id반환,맨 처음은 HomeMainFragment로 지정
            .commitAllowingStateLoss()


        viewBinding.bottomNav.run{
            setOnItemSelectedListener {
                when(it.itemId){
                    R.id.home_menu->{
                        supportFragmentManager
                            .beginTransaction().setCustomAnimations(
                                R.anim.fade_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.fade_out
                            )
                            .replace(viewBinding.containerFragment.id , HomeMainFragment())
                            .commitAllowingStateLoss()
                    }
                    R.id.vibe_menu->{
                        supportFragmentManager
                            .beginTransaction().setCustomAnimations(
                                R.anim.fade_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.fade_out
                            )
                            .replace(viewBinding.containerFragment.id , VibeMainFragment())
                            .commitAllowingStateLoss()
                    }
                    R.id.history_menu->{
                        supportFragmentManager
                            .beginTransaction().setCustomAnimations(
                                R.anim.fade_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.fade_out
                            )
                            .replace(viewBinding.containerFragment.id , HistoryMainFragment())
                            .commitAllowingStateLoss()
                    }
                }
                true
            }

            selectedItemId=R.id.home_menu
        }

        if(intent.extras?.getInt("frag_code")==3){
            supportFragmentManager
                .beginTransaction()
                .replace(viewBinding.containerFragment.id , HistoryMainFragment())
                .commitAllowingStateLoss()
        }


    }

    override fun onRestart() {
        super.onRestart()
        setDataInList()
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - waitTime >=1500 ) {
            waitTime = System.currentTimeMillis()
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show()
        } else {
            finish() // 액티비티 종료
        }
    }



}
