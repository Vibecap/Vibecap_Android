package com.example.vibecapandroid

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vibecapandroid.coms.*
import com.example.vibecapandroid.databinding.ActivityVibeCommentBinding
import com.example.vibecapandroid.databinding.DialogActivityVibeCommentAddSubCommentBinding
import com.example.vibecapandroid.databinding.ItemVibeCommentBinding
import com.example.vibecapandroid.utils.SoftKeyboardDetectorView
import com.example.vibecapandroid.utils.SoftKeyboardDetectorView.OnHiddenKeyboardListener
import com.example.vibecapandroid.utils.getRetrofit
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VibeCommentActivity : AppCompatActivity(), GetCommentsView, WriteCommentView,
    View.OnClickListener {

    lateinit var binding: ActivityVibeCommentBinding
    lateinit var itemBinding: ItemVibeCommentBinding
    private lateinit var getCommentsView: GetCommentsView
    private lateinit var writeCommentView: WriteCommentView
    private var isCommentWrote = false

    private var postId: Int? = 0
    private var commentId: Int = 0
    private var subCommentId: Int = 0

    var isSubCommentAddClicked = false  // 답글 달기 클릭 여부
    lateinit var mContext: Context      // Context 변수
    private var commentsResultData = ArrayList<CommentsResult>()

    var commentPosition = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVibeCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        itemBinding = ItemVibeCommentBinding.inflate(layoutInflater)

        mContext = this

        overridePendingTransition(R.anim.slide_in, R.anim.fade_out)

        setCommentView(this, this)

        val vibeCommentRVAdapter = VibeCommentRVAdapter(this, commentsResultData)
        binding.vibeCommentRv.adapter = vibeCommentRVAdapter

        // 전달된 데이터를 받을 Intent
        val intent = intent

        postId = intent.getIntExtra("post_id", 0)
        val postProfileImg: Bitmap? = intent.getParcelableExtra("post_profile_img")
        val postNickname = intent.getStringExtra("post_nickname")
        val postBody = intent.getStringExtra("post_body")
        val postDate = intent.getStringExtra("post_date")

        binding.vibeCommentWriterProfileIv.setImageBitmap(postProfileImg)
        binding.vibeCommentWriterNicknameTv.text = postNickname
        binding.vibeCommentPostBodyTv.text = postBody
        binding.vibeCommentWriterDateTv.text = postDate

        binding.vibeCommentBackBtn.setOnClickListener(this)
        binding.vibeCommentAddBtn.setOnClickListener(this)

        // 댓글 & 대댓글 조회
        getComments(userToken, postId!!)

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.vibeCommentBackBtn -> finish()
            binding.vibeCommentAddBtn -> {
                if (binding.vibeCommentAddEt.text.toString().replace(" ", "") == "") {
                    Toast.makeText(this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show()
                } else {
                    if (isSubCommentAddClicked) {  // 대댓글 입력 시
                        softKeyBoardHide(binding.vibeCommentAddEt)
                        writeSubComment(
                            commentId,
                            WriteSubCommentReq(MEMBER_ID, binding.vibeCommentAddEt.text.toString())
                        )
                        isSubCommentAddClicked = false
                    } else {  // 댓글 입력 시
                        softKeyBoardHide(binding.vibeCommentAddEt)
                        writeComment(
                            postId!!,
                            WriteCommentReq(MEMBER_ID, binding.vibeCommentAddEt.text.toString())
                        )
                    }
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out)
    }

    private fun softKeyBoardHide(editText: EditText?) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText!!.windowToken, 0)
    }

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
        getCommentsView: GetCommentsView,
        writeCommentView: WriteCommentView
    ) {
        this.getCommentsView = getCommentsView
        this.writeCommentView = writeCommentView
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
    private fun writeComment(postId: Int, writeCommentReq: WriteCommentReq) {
        val vibeCommentService = getRetrofit().create(VibeCommentApiInterface::class.java)
        vibeCommentService.writeComment(userToken, postId, writeCommentReq)
            .enqueue(object : Callback<WriteCommentResponse> {
                override fun onResponse(
                    call: Call<WriteCommentResponse>,
                    response: Response<WriteCommentResponse>
                ) {
                    Log.d("[VIBE] WRITE_COMMENT/SUCCESS", response.toString())
                    val resp: WriteCommentResponse = response.body()!!

                    Log.d("[VIBE] WRITE_COMMENT/CODE", resp.code.toString())

                    // 서버 response 중 code 값에 따른 결과
                    when (resp.code) {
                        1000 -> writeCommentView.onWriteCommentSuccess(resp.result)
                        else -> writeCommentView.onWriteCommentFailure(resp.code, resp.message)
                    }
                }

                override fun onFailure(call: Call<WriteCommentResponse>, t: Throwable) {
                    Log.d("[VIBE] WRITE_COMMENT/FAILURE", t.message.toString())
                }
            })
        Log.d("[VIBE] WRITE_COMMENT", "HELLO")
    }

    /**
     * 대댓글 작성
     */
    private fun writeSubComment(commentId: Int, writeSubCommentReq: WriteSubCommentReq) {
        val vibeCommentService = getRetrofit().create(VibeCommentApiInterface::class.java)
        vibeCommentService.writeSubComment(userToken, commentId, writeSubCommentReq)
            .enqueue(object : Callback<WriteSubCommentResponse> {
                override fun onResponse(
                    call: Call<WriteSubCommentResponse>,
                    response: Response<WriteSubCommentResponse>
                ) {
                    Log.d("[VIBE] WRITE_SUB_COMMENT/SUCCESS", response.toString())
                    val resp: WriteSubCommentResponse = response.body()!!

                    Log.d("[VIBE] WRITE_SUB_COMMENT/CODE", resp.code.toString())

                    // 서버 response 중 code 값에 따른 결과
                    when (resp.code) {
                        1000 -> {
                            binding.vibeCommentAddEt.text.clear()
                            getComments(userToken, postId!!)
                            subCommentId = resp.result.subCommentId.toInt()
                        }
                        else -> {
                            Log.d(
                                "[VIBE] WRITE_SUB_COMMENT/FAILURE",
                                "${resp.code} / ${resp.message}"
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<WriteSubCommentResponse>, t: Throwable) {
                    Log.d("[VIBE] WRITE_SUB_COMMENT/FAILURE", t.message.toString())
                }
            })
        Log.d("[VIBE] WRITE_SUB_COMMENT", "HELLO")
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

        // 새 댓글이 등록됐으면 마지막 댓글 위치로 이동
        if (isCommentWrote) {
            binding.vibeCommentRv.smoothScrollToPosition(result.size - 1)
            isCommentWrote = false
        }


        val softKeyboardDetector = SoftKeyboardDetectorView(mContext)
        addContentView(softKeyboardDetector, FrameLayout.LayoutParams(-1, -1))

        vibeCommentRVAdapter.setMyItemClickListener(object :
            VibeCommentRVAdapter.MyItemClickListener {
            override fun onItemClick(commentsResult: CommentsResult) {
            }

            override fun onSubCommentAddClick(position: Int) {
                // 대댓글 작성 Dialog 표시
                val dialogBinding =
                    DialogActivityVibeCommentAddSubCommentBinding.inflate(layoutInflater)
                val dialog = Dialog(mContext)

                dialog.setContentView(dialogBinding.root)
                dialog.setCancelable(false)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()

                // 대댓글 작성 확인 버튼
                dialogBinding.dialogVibeCommentAddSubCommentAdd.setOnClickListener {
                    itemBinding.itemVibeCommentLayout.setBackgroundResource(R.color.light_purple)
                    dialog.dismiss()

                    // 키보드 올리기
                    val manager: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    binding.vibeCommentAddEt.postDelayed(Runnable {
                        binding.vibeCommentAddEt.requestFocus()
                        manager.showSoftInput(binding.vibeCommentAddEt, 0)
                    }, 100)

                    isSubCommentAddClicked = true
                    commentPosition = position
                    // 키보드 사라질 때
                    softKeyboardDetector.setOnHiddenKeyboard(object : OnHiddenKeyboardListener {
                        override fun onHiddenSoftKeyboard() {
                            itemBinding.itemVibeCommentLayout.setBackgroundResource(R.color.white)
                            vibeCommentRVAdapter.notifyItemChanged(position)
                            isSubCommentAddClicked = false
                        }
                    })
                }

                // 대댓글 작성 취소 버튼
                dialogBinding.dialogVibeCommentAddSubCommentCancel.setOnClickListener {
                    isSubCommentAddClicked = false
                    dialog.dismiss()
                    itemBinding.itemVibeCommentLayout.setBackgroundResource(R.color.white)
                    vibeCommentRVAdapter.notifyItemChanged(position, Unit)
                }

                commentId = result[position].commentId.toInt()
            }
        })

        binding.vibeCommentRv.adapter = vibeCommentRVAdapter
    }

    override fun onGetCommentsFailure(code: Int, message: String) {
        Log.d("[VIBE] GET_COMMENTS/FAILURE", "$code / $message")
    }


    /**
     * 댓글 작성 성공, 실패 처리
     */
    override fun onWriteCommentSuccess(result: WriteCommentResult) {
        binding.vibeCommentAddEt.text.clear()
        isCommentWrote = true
        getComments(userToken, postId!!)
    }

    override fun onWriteCommentFailure(code: Int, message: String) {
        Log.d("[VIBE] WRITE_COMMENTS/FAILURE", "$code / $message")
    }

}

interface GetCommentsView {
    fun onGetCommentsSuccess(result: ArrayList<CommentsResult>)
    fun onGetCommentsFailure(code: Int, message: String)
}

interface WriteCommentView {
    fun onWriteCommentSuccess(result: WriteCommentResult)
    fun onWriteCommentFailure(code: Int, message: String)
}