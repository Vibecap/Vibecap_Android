package com.example.vibecapandroid

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.PostDetailData
import com.example.vibecapandroid.coms.PostDetailResponse
import com.example.vibecapandroid.coms.VibePostApiInterface
import com.example.vibecapandroid.databinding.ActivityVibePostBinding
import com.example.vibecapandroid.utils.getRetrofit
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.*

class VibePostActivity : AppCompatActivity(), GetPostView, View.OnClickListener {

    lateinit var binding: ActivityVibePostBinding
    private lateinit var getPostView: GetPostView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVibePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 게시물 1개 조회
        getPost()

        binding.vibePostBackBtn.setOnClickListener(this)
        binding.vibePostCommentBtn.setOnClickListener(this)

        // 게시물 메뉴 BottomSheet 설정
        val postMenuBottomSheetView =
            layoutInflater.inflate(R.layout.bottom_sheet_vibe_post_menu, binding.root, false)
        val postMenuBottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialog)

        postMenuBottomSheetDialog.setContentView(postMenuBottomSheetView)
        setBottomSheetView(postMenuBottomSheetView, postMenuBottomSheetDialog, this)

        binding.vibePostMenuBtn.setOnClickListener {
            postMenuBottomSheetDialog.show()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.vibePostBackBtn -> finish()
            binding.vibePostCommentBtn -> {
                // 댓글창 열기 // post_id 전달 해야 함
                val intent = Intent(this, VibeCommentActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // 게시물 메뉴 BottomSheet Click event 설정
    private fun setBottomSheetView(
        bottomSheetView: View,
        dialog: BottomSheetDialog,
        context: Context
    ) {
        val postBlockBtn =
            bottomSheetView.findViewById<ConstraintLayout>(R.id.bottom_sheet_vibe_post_block_layout)
        postBlockBtn.setOnClickListener {
            Toast.makeText(context, "게시물을 차단했습니다.", Toast.LENGTH_SHORT).show()
            // 차단 API
        }

        val postReportBtn =
            bottomSheetView.findViewById<ConstraintLayout>(R.id.bottom_sheet_vibe_post_report_layout)
        postReportBtn.setOnClickListener {
            val intent = Intent(context, VibePopupActivity::class.java)
            startActivity(intent)
        }

        val postLinkBtn =
            bottomSheetView.findViewById<ConstraintLayout>(R.id.bottom_sheet_vibe_post_link_layout)
        postLinkBtn.setOnClickListener {
            Toast.makeText(context, "링크를 복사했습니다.", Toast.LENGTH_SHORT).show()
            // 링크 복사 API
        }

        val postShareBtn =
            bottomSheetView.findViewById<ConstraintLayout>(R.id.bottom_sheet_vibe_post_share_layout)
        postShareBtn.setOnClickListener {
            Toast.makeText(context, "게시물을 공유헀습니다.", Toast.LENGTH_SHORT).show()
            // 게시물 공유 API
        }

        // 상단 close bar 버튼 누르면 닫기
        val closeBtn =
            bottomSheetView.findViewById<ImageButton>(R.id.bottom_sheet_vibe_post_menu_close_btn)
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }
    }

    // 게시물 1개 조회
    private fun getPost() {
        val vibePostService = getRetrofit().create(VibePostApiInterface::class.java)
        vibePostService.postDetailCheck(userToken, 33)
            .enqueue(object : Callback<PostDetailResponse> {
                override fun onResponse(
                    call: Call<PostDetailResponse>,
                    response: Response<PostDetailResponse>
                ) {
                    Log.d("[VIBE] GET_POST/SUCCESS", response.toString())
                    val resp: PostDetailResponse = response.body()!!

                    Log.d("[VIBE] GET_POST/CODE", resp.code.toString())

                    // 서버 response 중 code 값에 따른 결과
                    when (resp.code) {
                        1000 -> getPostView.onGetPostSuccess(resp.result)
                        else -> getPostView.onGetPostFailure(resp.code, resp.message)
                    }
                }

                override fun onFailure(call: Call<PostDetailResponse>, t: Throwable) {
                    Log.d("[VIBE] GET_POST/FAILURE", t.message.toString())
                }
            })
        Log.d("[VIBE] GET_POST", "HELLO")
    }

    // 게시물 설정
    private fun setPost(result: PostDetailData) {
        binding.vibePostPostTitleTv.text = result.title
        binding.vibePostPostBodyTv.text = result.body
        binding.vibePostNicknameTv.text = result.nickname
        Glide.with(applicationContext).load(result.profileImg).into(binding.vibePostProfileImgBtn)
        binding.vibePostDateTv.text = result.modifiedDate.toString()
        // ++ tag name 설정 (최대 최소 제한 있는지? / 뭔가 1을 만나면 태그가 끝나는 듯)

        val beginIdx = result.youtubeLink.indexOf("watch?v=")
        val endIdx = result.youtubeLink.length
        val videoId = result.youtubeLink.substring(beginIdx + 8, endIdx)
        val youtubePlayerFragment = YoutubePlayerFragment.newInstance()
        val bundle = Bundle()
        bundle.putString("VIDEO_ID", videoId)
        youtubePlayerFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.vibe_post_youtube_player_view, youtubePlayerFragment)
            .commitNow()

        binding.vibePostLikeCountTv.text = result.likeNumber.toString()
        binding.vibePostCommentCountTv.text = result.commentNumber.toString()
    }

    override fun onGetPostSuccess(result: PostDetailData) {
        setPost(result)
    }

    override fun onGetPostFailure(code: Int, message: String) {
        Log.d("[VIBE] GET_POST/FAILURE", "$code / $message")
    }


}

interface GetPostView {
    fun onGetPostSuccess(result: PostDetailData)
    fun onGetPostFailure(code: Int, message: String)
}

