package com.example.vibecapandroid


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.vibecapandroid.databinding.ActivityMainBinding

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
class MainActivity : AppCompatActivity() {
    private val viewBinding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    }
                }
                true
            }

            selectedItemId=R.id.home_menu
        }

        /*
        val fragment_home_main_alarm = findViewById<Button>(R.id.fragment_home_main_alarm)
        fragment_home_main_alarm.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MypageActlistActivity::class.java)
            startActivity(intent)
        })

 */

        val fragment_home_main_mypage = findViewById<Button>(R.id.fragment_home_main_mypage)
        fragment_home_main_mypage.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MypageProfileActivity::class.java)
            startActivity(intent)
        })
        val fragment_home_main_alarm = findViewById<Button>(R.id.fragment_home_main_alarm)
        fragment_home_main_alarm.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MypageAlarmActivity::class.java)
            startActivity(intent)
        })

    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item and false if the item should not be
     * selected. Consider setting non-selectable items as disabled preemptively to make them
     * appear non-interactive.
     */


}