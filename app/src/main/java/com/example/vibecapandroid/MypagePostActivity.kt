package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import com.example.vibecapandroid.coms.*
import com.example.vibecapandroid.databinding.ActivityMypagePostBinding
import com.example.vibecapandroid.databinding.ActivityVibePostBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

class MypagePostActivity : AppCompatActivity() {

    private val viewBinding: ActivityMypagePostBinding by lazy {
        ActivityMypagePostBinding.inflate(layoutInflater)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        val postId = intent.extras?.getInt("post_id")
        Log.d("postId","${postId}")

        //임시(수정하기)
        viewBinding.imageViewProfile.setOnClickListener {
            val intent = Intent(this, HistoryEditActivity::class.java)
            startActivity(intent)
        }

        viewBinding.imageButtonComment.setOnClickListener {
            val intent = Intent(this, VibeCommentActivity::class.java)
            startActivity(intent)
        }
        viewBinding.acitityMypagePostBack.setOnClickListener{
            val intent = Intent(this, MypageWritedActivity::class.java)
            startActivity(intent)
        }
        //popup

        viewBinding.articlePopup.setOnClickListener{
        val bottomSheetView = layoutInflater.inflate(R.layout.fragment_mypage_post_popup_edit, null)
        val bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bottomSheetDialog.show()

            val dialog = MypagePostPopupEditFragment()
            dialog.setButtonClickListener(object :MypagePostPopupEditFragment.OnButtonClickListener{
                override fun onButton1Clicked() {

                }

                override fun onButton2Clicked() {
                }

            })

        }



        //웹 브라우저 창 열기
        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //어떤 주소로 들어갈지 입력
        val apiService = retrofit.create(MypageApiInterface::class.java)

        //입력한 주소중 하나로 연결 시도
        if (postId != null) {
            apiService.getMypagePost(postId).enqueue(object :
                Callback<postMypageResponse> {
                @SuppressLint("ResourceType")
                override fun onResponse(
                    call: Call<postMypageResponse>,
                    response: Response<postMypageResponse>
                ) {

                    if (response.isSuccessful) {

                        val responseData = response.body()
                        Log.d(
                            "Retrofit",
                            "MypageResponse\n" +
                                    "isSuccess:${responseData?.is_success}" +
                                    "Code:${responseData?.code}" +
                                    "Message:${responseData?.message}" +
                                    "Result:${responseData?.result}"
                        )

                        if (responseData !== null) {

                            Log.d(
                                "Retrofit",
                                "MypageResponse\n" +
                                        "isSuccess:${responseData.is_success}" +
                                        "Code:${responseData.code}" +
                                        "Message:${responseData.message}" +
                                        "Result:${responseData.result}"
                            )
                            if (responseData.is_success) {
                                Log.d("responseData", "${responseData.result}")
                                if (responseData.result==null) {
                                    Log.d("게시된 사진 없음", "게시된 사진 없음")
                                } else {

                                    viewBinding.textViewUsername.text= responseData.result.nickname
                                    viewBinding.textViewTitle.text=responseData.result.title
                                    viewBinding.textViewPosttxt.text=responseData.result.body
                                    viewBinding.imageViewPostmain.setImageResource(R.drawable.image_ic_activity_history_album_list2)
                                    viewBinding.imageViewProfile.setImageResource(R.drawable.image_ic_activity_history_album_list3)
                                    viewBinding.textViewTag1.text=responseData.result.tag_name



                                }
                            }


                        } else {
                            Log.d("Retrofit", "Null data")
                        }

                    } else {
                        Log.w("MypagePostActivity Retrofit", "Response Not Successful${response.code()}")
                    }
                }

                override fun onFailure(call: Call<postMypageResponse>, t: Throwable) {
                    Log.d("mypagePost","${t.message.toString()}")

                }
            })
        }
    }


}








