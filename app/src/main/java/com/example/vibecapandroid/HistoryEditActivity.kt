package com.example.vibecapandroid

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.vibecapandroid.coms.*
import com.example.vibecapandroid.databinding.ActivityHistoryEditBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HistoryEditActivity : AppCompatActivity() {
    private val viewBinding: ActivityHistoryEditBinding by lazy {
        ActivityHistoryEditBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_edit)
        setContentView(viewBinding.root)

        val postId = intent.extras?.getInt("post_id")
        Log.d("postId", "${postId}")

        viewBinding.activityHistoryPostFinish.setOnClickListener() {


            val intent = Intent(this, MypageWritedActivity::class.java)
            startActivity(intent)
        }
        viewBinding.activityHistoryPostCancel.setOnClickListener() {
            val dialogBinding =
                layoutInflater.inflate(R.layout.activity_history_postedit_dialog_cancel, null)

            val logoutDialog = Dialog(this)
            logoutDialog.setContentView(dialogBinding)
            logoutDialog.setCancelable(true)
            logoutDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            logoutDialog.show()

            val cancelBtn = dialogBinding.findViewById<Button>(R.id.dialog_postdedit_cancel)
            cancelBtn.setOnClickListener {
                val intent = Intent(this, HistoryEditActivity::class.java)
                startActivity(intent)
            }

            val okayBtn = dialogBinding.findViewById<Button>(R.id.dialog_postedit_ok)
            okayBtn.setOnClickListener {
                val intent = Intent(this, MypageWritedActivity::class.java)
                startActivity(intent)
            }

        }
        //웹 브라우저 창 열기
        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //어떤 주소로 들어갈지 입력
        val apiService = retrofit.create(MypageApiInterface::class.java)


/*
        if (postId != null) {
            apiService.patchMypageEditPost(
                userToken, postId,
                patchMypageEditPostInput(
                    MEMBER_ID.toInt(),
                    findViewById<EditText>(R.id.activity_history_post_title).text.toString(),
                    findViewById<EditText>(R.id.activity_history_post_text).text.toString()
                )
            ).enqueue(object :
                Callback<patchMypageEditResponse> {
                override fun onResponse(
                    call: Call<patchMypageEditResponse>,
                    response: Response<patchMypageEditResponse>
                ) {

                    if (response.isSuccessful) {
                        val responseData = response.body()

                        if (responseData !== null) {
                            Log.d(
                                "Retrofit",
                                "MypageNicknameResponse\n" +
                                        "isSuccess:${responseData.is_success}" +
                                        "Code:${responseData.code}" +
                                        "Message:${responseData.message}" +
                                        "Result:${responseData.result}"
                            )

                            viewBinding.textViewUsername.text= responseData.result
                            viewBinding.imageViewPostmain.setImageResource(R.drawable.image_ic_activity_history_album_list2)
                            viewBinding.imageViewProfile.setImageResource(R.drawable.image_ic_activity_history_album_list3)
                            viewBinding.textViewTag1.text=responseData.result




                        } else {
                            Log.d("Retrofit", "Null data")
                        }

                    } else {
                        Log.w("Retrofit", "Response Not Successful${response.code()}")
                    }
                }

                override fun onFailure(call: Call<patchMypageEditResponse>, t: Throwable) {
                    Log.e("Retrofit", "Error", t)
                }


            })
        }




*/
    }
}
