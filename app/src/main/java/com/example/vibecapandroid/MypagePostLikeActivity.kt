package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.widget.AppCompatImageButton
import com.example.vibecapandroid.coms.MypageApiInterface
import com.example.vibecapandroid.coms.postMypageLikeResponse
import com.example.vibecapandroid.coms.postMypageResponse
import com.example.vibecapandroid.databinding.ActivityMypagePostLikeBinding
import com.example.vibecapandroid.databinding.FragmentMypagePostPopupBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MypagePostLikeActivity : AppCompatActivity() {

    private val viewBinding: ActivityMypagePostLikeBinding by lazy {
        ActivityMypagePostLikeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_post_like)
        setContentView(viewBinding.root)


        val postId = intent.extras!!.getInt("post_id")
        Log.d("postId","${postId}")

        viewBinding.imageButtonComment.setOnClickListener {
            val intent = Intent(this, VibeCommentActivity::class.java)
            startActivity(intent)
        }
        viewBinding.acitityMypagePostBack.setOnClickListener{
            val intent = Intent(this, MypageLikeActivity::class.java)
            startActivity(intent)
        }

        //popup
        viewBinding.articlePopup.setOnClickListener{
        val bottomSheetView = layoutInflater.inflate(R.layout.fragment_mypage_post_popup, null)
            val bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bottomSheetDialog.show()

        }



        //웹 브라우저 창 열기
        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //어떤 주소로 들어갈지 입력
        val apiService = retrofit.create(MypageApiInterface::class.java)

        //입력한 주소중 하나로 연결 시도
        apiService.getMypageLikePost(postId).enqueue(object :
            Callback<postMypageLikeResponse> {
            @SuppressLint("ResourceType")
            override fun onResponse(
                call: Call<postMypageLikeResponse>,
                response: Response<postMypageLikeResponse>
            ) {

                if (response.isSuccessful) {
                    val responseData = response.body()

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
                            if (responseData.result.isEmpty()) {
                                Log.d("게시된 사진 없음", "게시된 사진 없음")
                            } else {

                                viewBinding.textViewUsername.text= responseData.result[0].nickname
                                viewBinding.textViewTitle.text=responseData.result[0].title
                                viewBinding.textViewPosttxt.text=responseData.result[0].body
                                viewBinding.imageViewPostmain.setImageResource(R.drawable.image_ic_activity_history_album_list2)
                                viewBinding.imageViewProfile.setImageResource(R.drawable.image_ic_activity_history_album_list3)
                                viewBinding.textViewTag1.text=responseData.result[0].tag_name



                            }
                        }


                    } else {
                        Log.d("Retrofit", "Null data")
                    }

                } else {
                    Log.w("MypagePostLikeActivity Retrofit", "Response Not Successful${response.code()}")
                }
            }

            override fun onFailure(call: Call<postMypageLikeResponse>, t: Throwable) {

            }
        })
    }


}