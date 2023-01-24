package com.example.vibecapandroid



import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

import android.content.ContentValues.TAG
import android.content.Context

import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.vibecapandroid.coms.MypageApiInterface
import com.example.vibecapandroid.coms.CheckMypageResponse

import com.example.vibecapandroid.databinding.ActivityMainBinding
import java.util.SimpleTimeZone
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

class MainActivity : AppCompatActivity() {

    private val viewBinding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    @SuppressLint("WrongViewCast")
    lateinit var userToken:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_VibecapAndroid)
        // 이 부분은 splash 로

        var isLoggedIn=getSharedPreferences("sharedprefs", Context.MODE_PRIVATE).getBoolean("isLoggedIn",false)
        if(!isLoggedIn){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            userToken=getSharedPreferences("sharedprefs",Context.MODE_PRIVATE).getString("Token","none")!!
            Log.d("Token","$userToken")
        }
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
                            .beginTransaction()
                            .replace(viewBinding.containerFragment.id , HomeMainFragment())
                            .commitAllowingStateLoss()
                    }
                    R.id.vibe_menu->{
                        supportFragmentManager
                            .beginTransaction()
                            .replace(viewBinding.containerFragment.id , VibeMainFragment())
                            .commitAllowingStateLoss()
                    }
                    R.id.history_menu->{
                        supportFragmentManager
                            .beginTransaction()
                            .replace(viewBinding.containerFragment.id , HistoryMainFragment())
                            .commitAllowingStateLoss()
                        // 이 부분 수정필요
                        val editor=getSharedPreferences("sharedprefs", Context.MODE_PRIVATE).edit()
                        editor.remove("isLoggedIn")
                        editor.apply()
                    }
                }
                true
            }

            selectedItemId=R.id.home_menu
        }



    }


   /* override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"Destroy")
        val editor=getSharedPreferences("sharedprefs", Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.commit()
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG,"Restart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"Stop")
        val editor=getSharedPreferences("sharedprefs", Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.commit()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"Start")
    }*/
    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item and false if the item should not be
     * selected. Consider setting non-selectable items as disabled preemptively to make them
     * appear non-interactive.
     */


}