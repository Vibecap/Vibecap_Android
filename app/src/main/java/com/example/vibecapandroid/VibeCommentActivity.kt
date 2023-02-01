package com.example.vibecapandroid

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vibecapandroid.coms.*
import com.example.vibecapandroid.databinding.ActivityVibeCommentBinding
import com.example.vibecapandroid.utils.getRetrofit
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VibeCommentActivity : AppCompatActivity(), GetCommentsView, View.OnClickListener {

    lateinit var binding: ActivityVibeCommentBinding
    private lateinit var getCommentsView: GetCommentsView
    private var postId:Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVibeCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCommentView(this)

        val intent = intent // 전달된 데이터를 받을 Intent
        postId = intent.getIntExtra("post_id", 0)
        val postProfileImg: Bitmap? = intent.getParcelableExtra("post_profile_img")
        val postNickname = intent.getStringExtra("post_nickname")
        val postBody = intent.getStringExtra("post_body")
        val postDate = intent.getStringExtra("post_date")

        binding.vibeCommentWriterProfileIv.setImageBitmap(postProfileImg)
        binding.vibeCommentWriterNicknameTv.text = postNickname
        binding.vibeCommentPostBodyTv.text = postBody
        binding.vibeCommentWriterDateTv.text = postDate

        // 댓글 & 대댓글 조회
        getComments(userToken, postId!!)

        // 댓글 작성

        binding.vibeCommentBackBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.vibeCommentBackBtn -> finish()
        }
    }

    override fun finish() {
        val intent = Intent(this, VibePostActivity::class.java)
        intent.putExtra("post_id",postId)
        startActivity(intent)
        super.finish()
    }

    /**
     * 댓글 & 대댓글 조회
     */
    private fun getComments(userToken: String, postId: Int) {
        val vibeCommentService = getRetrofit().create(VibeCommentApiInterface::class.java)
        vibeCommentService.getComments(userToken, postId)
            .enqueue(object : Callback<GetCommentsResponse> {
                override fun onResponse(
                    call: Call<GetCommentsResponse>,
                    response: Response<GetCommentsResponse>
                ) {
                    Log.d("[VIBE] GET_COMMENTS/SUCCESS", response.toString())
                    val resp: GetCommentsResponse = response.body()!!

                    Log.d("[VIBE] GET_COMMENTS/CODE", resp.code.toString())

                    // 서버 response 중 code 값에 따른 결과
                    when (resp.code) {
                        1000 -> getCommentsView.onGetCommentsSuccess(resp.result)
                        else -> getCommentsView.onGetCommentsFailure(resp.code, resp.message)
                    }
                }

                override fun onFailure(call: Call<GetCommentsResponse>, t: Throwable) {
                    Log.d("[VIBE] GET_COMMENTS/FAILURE", t.message.toString())
                }
            })
        Log.d("[VIBE] GET_COMMENTS", "HELLO")
    }

    /**
     * 댓글 작성
     */


    // 댓글 메뉴 BottomSheet Click event 설정
    private fun setCommentBottomSheetView(
        bottomSheetView: View,
        dialog: BottomSheetDialog,
        context: Context
    ) {
        val postBlockBtn =
            bottomSheetView.findViewById<ConstraintLayout>(R.id.bottom_sheet_vibe_post_block_layout)
        postBlockBtn.setOnClickListener {
            Toast.makeText(context, "댓글을 차단했습니다.", Toast.LENGTH_SHORT).show()
            // 차단 API
        }

        val postReportBtn =
            bottomSheetView.findViewById<ConstraintLayout>(R.id.bottom_sheet_vibe_post_report_layout)
        postReportBtn.setOnClickListener {
            Toast.makeText(context, "게시물을 차단했습니다.", Toast.LENGTH_SHORT).show()

        }

        // 상단 close bar 버튼 누르면 닫기
        val closeBtn =
            bottomSheetView.findViewById<ImageButton>(R.id.bottom_sheet_vibe_post_menu_close_btn)
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun setCommentView(
        getCommentsView: GetCommentsView
    ) {
        this.getCommentsView = getCommentsView
    }

    /**
     * 댓글 & 대댓글 조회 성공, 실패 처리
     */
    override fun onGetCommentsSuccess(result: ArrayList<CommentsResult>) {
        // 댓글 rv
        val vibeCommentRVAdapter = VibeCommentRVAdapter(this, result)
        binding.vibeCommentRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.vibeCommentRv.adapter = vibeCommentRVAdapter

    }

    override fun onGetCommentsFailure(code: Int, message: String) {
        Log.d("[VIBE] GET_COMMENTS/FAILURE", "$code / $message")
    }

}

interface GetCommentsView {
    fun onGetCommentsSuccess(result: ArrayList<CommentsResult>)
    fun onGetCommentsFailure(code: Int, message: String)
}