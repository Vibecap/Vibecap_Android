package com.example.vibecapandroid

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import com.example.vibecapandroid.coms.MypageApiInterface
import com.example.vibecapandroid.databinding.FragmentHomeMainBinding
import com.example.vibecapandroid.databinding.FragmentMypagePostPopupEditBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MypagePostPopupEditFragment : Fragment() {
    private lateinit var viewBinding: FragmentMypagePostPopupEditBinding





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMypagePostPopupEditBinding.inflate(layoutInflater)

        viewBinding.textViewDeclaration.setOnClickListener{
            Log.d("edit","click")
            val intent = Intent(context, HistoryEditActivity::class.java)
            startActivity(intent)
            buttonClickListener.onButton1Clicked()
        }
        viewBinding.dialogTableDelete.setOnClickListener{


            //웹 브라우저 창 열기
            val retrofit = Retrofit.Builder()
                .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            //어떤 주소로 들어갈지 입력
            val apiService = retrofit.create(MypageApiInterface::class.java)

            val intent = Intent(context, MypageProfileActivity::class.java)
            startActivity(intent)
            buttonClickListener.onButton2Clicked()


        }


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage_post_popup_edit, container, false)


    }
    interface OnButtonClickListener {
        fun onButton1Clicked()
        fun onButton2Clicked()
    }
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }
    // 클릭 이벤트 실행
    private lateinit var buttonClickListener: OnButtonClickListener
}



