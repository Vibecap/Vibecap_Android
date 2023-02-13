package com.example.vibecapandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.vibecapandroid.coms.*
import com.example.vibecapandroid.databinding.ActivityMypageAlarmBinding
import com.example.vibecapandroid.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageAlarmActivity : AppCompatActivity() {

    lateinit var binding: ActivityMypageAlarmBinding
    private lateinit var mypageAlarmRVAdapter: MypageAlarmRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        overridePendingTransition(R.anim.slide_in, R.anim.fade_out)

        binding.mypageAlarmBackBtn.setOnClickListener { finish() }

        // 활동 내역 RecyclerView adapter 연결
        mypageAlarmRVAdapter = MypageAlarmRVAdapter(this)
        binding.mypageAlarmRv.adapter = mypageAlarmRVAdapter

        // 활동 내역 조회
        getAlarmList()

        // 알림 클릭 시
        mypageAlarmRVAdapter.setMyItemClickListener(object :
            MypageAlarmRVAdapter.MyItemClickListener {
            override fun onItemClick(alarmList: Notice) {
                // 알림 읽음 처리
                deleteAlarm(alarmList.event, alarmList.noticeId)

                // 해당 게시물로 이동
                val intent = Intent(this@MypageAlarmActivity, VibePostActivity::class.java)
                intent.putExtra("post_id", alarmList.postId.toInt())
                startActivity(intent)
            }
        })

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out)
    }

    override fun onResume() {
        super.onResume()
        getAlarmList()
    }

    /**
     * 활동 내역 (알림) 조회
     */
    private fun getAlarmList() {
        val mypageService = getRetrofit().create(MypageApiInterface::class.java)
        mypageService.getAlarmHistory(userToken, MEMBER_ID)
            .enqueue(object : Callback<GetAlarmHistoryResponse> {
                override fun onResponse(
                    call: Call<GetAlarmHistoryResponse>,
                    response: Response<GetAlarmHistoryResponse>,
                ) {
                    Log.d("[MYPAGE] GET_ALARM_LIST/SUCCESS", response.toString())
                    val resp: GetAlarmHistoryResponse = response.body()!!

                    // 서버 response 중 code 값에 따른 결과
                    when (resp.code) {
                        1000 -> {
                                mypageAlarmRVAdapter.setPosts(resp.result)
                        }
                        else -> Log.d(
                            "[MYPAGE] GET_ALARM_LIST/FAILURE",
                            "${resp.code} / ${resp.message}"
                        )
                    }
                }

                override fun onFailure(call: Call<GetAlarmHistoryResponse>, t: Throwable) {
                    Log.d("[MYPAGE] GET_ALARM_LIST/FAILURE", t.message.toString())
                }
            })
    }

    /**
     * 활동 내역 (알림) 읽음 처리
     */
    private fun deleteAlarm(type: String, noticeId: Long) {
        val mypageService = getRetrofit().create(MypageApiInterface::class.java)
        mypageService.deleteAlarm(userToken, type, noticeId)
            .enqueue(object : Callback<DeleteAlarmResponse> {
                override fun onResponse(
                    call: Call<DeleteAlarmResponse>,
                    response: Response<DeleteAlarmResponse>,
                ) {
                    Log.d("[MYPAGE] DELETE_ALARM/SUCCESS", response.toString())
                    val resp: DeleteAlarmResponse = response.body()!!

                    // 서버 response 중 code 값에 따른 결과
                    when (resp.code) {
                        1000 -> {
                            Toast.makeText(
                                this@MypageAlarmActivity,
                                "알림을 읽었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> Log.d(
                            "[MYPAGE] DELETE_ALARM/FAILURE",
                            "${resp.code} / ${resp.message}"
                        )
                    }
                }

                override fun onFailure(call: Call<DeleteAlarmResponse>, t: Throwable) {
                    Log.d("[MYPAGE] DELETE_ALARM/FAILURE", t.message.toString())
                }
            })
    }
}
