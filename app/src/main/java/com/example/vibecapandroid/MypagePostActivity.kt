package com.example.vibecapandroid

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.*
import com.example.vibecapandroid.databinding.ActivityVibePostBinding
import com.example.vibecapandroid.utils.getRetrofit
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import retrofit2.*


class MypagePostActivity : AppCompatActivity(), GetPostView, SetLikeView, SetScrapView,
    View.OnClickListener {

    private var postMenuBottomSheetDialog :BottomSheetDialog?=null
    lateinit var binding: ActivityVibePostBinding
    private lateinit var getPostView: GetPostView
    private lateinit var setLikeView: SetLikeView
    private lateinit var setScrapView: SetScrapView
    private var postId:Int?=0
    val memberId: MemberId = MemberId(MEMBER_ID)
    private var fullvideoId:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVibePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPostView(this, this, this)

        // 게시물 1개 조회
        /*** 전달 값은 postId */
        val intent = intent // 전달된 데이터를 받을 Intent
        postId = intent.getIntExtra("post_id", 0)
        val memberId: MemberId = MemberId(MEMBER_ID)

        getPost(postId!!, MEMBER_ID)

        binding.vibePostBackBtn.setOnClickListener(){

            this@MypagePostActivity.finish()
        }
        // 게시물 좋아요
        binding.vibePostLikeBtn.setOnClickListener {
            setLike(userToken, postId!!, memberId)
        }
        // 게시물 스크랩
        binding.vibePostScrapBtn.setOnClickListener {
            setScrap(userToken, postId!!, memberId)
        }
        // 댓글창
        binding.vibePostCommentBtn.setOnClickListener {
            finish()
            val intent = Intent(this, VibeCommentActivity::class.java)
            // 게시물의 post_id, 프로필 사진, 닉네임, 내용을 intent로 전달
            intent.putExtra("post_id", postId)
            val postProfileImgBitmap =
                (binding.vibePostProfileIv.drawable as BitmapDrawable).bitmap

            intent.putExtra("post_profile_img", postProfileImgBitmap)
            intent.putExtra("post_nickname", binding.vibePostNicknameTv.text)
            intent.putExtra("post_body", binding.vibePostPostBodyTv.text)
            intent.putExtra("post_date", binding.vibePostDateTv.text)

            startActivity(intent)
        }
        // 댓글창
        binding.vibePostCommentMoreBtn.setOnClickListener {
            finish()
            val intent = Intent(this, VibeCommentActivity::class.java)
            // 게시물의 post_id, 프로필 사진, 닉네임, 내용을 intent로 전달
            intent.putExtra("post_id", postId)
            val postProfileImgBitmap =
                (binding.vibePostProfileIv.drawable as BitmapDrawable).bitmap

            intent.putExtra("post_profile_img", postProfileImgBitmap)
            intent.putExtra("post_nickname", binding.vibePostNicknameTv.text)
            intent.putExtra("post_body", binding.vibePostPostBodyTv.text)
            intent.putExtra("post_date", binding.vibePostDateTv.text)

            startActivity(intent)
        }


        // 게시물 메뉴 BottomSheet 설정
        val postMenuBottomSheetView =
            layoutInflater.inflate(R.layout.fragment_mypage_post_popup_edit, binding.root, false)
        postMenuBottomSheetDialog =
            BottomSheetDialog(this@MypagePostActivity, R.style.CustomBottomSheetDialog)

        postMenuBottomSheetDialog!!.setContentView(postMenuBottomSheetView)
        setPostBottomSheetView(postMenuBottomSheetView, postMenuBottomSheetDialog!!, this)

        binding.vibePostMenuBtn.setOnClickListener {
            postMenuBottomSheetDialog!!.show()
        }

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.vibePostBackBtn -> finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(postMenuBottomSheetDialog!=null&&postMenuBottomSheetDialog!!.isShowing){
            postMenuBottomSheetDialog!!.dismiss()
        }
    }

    /*** 게시물 1개 조회 */
    private fun getPost(postId: Int, memberId: Long) {
        val vibePostService = getRetrofit().create(VibePostApiInterface::class.java)
        vibePostService.postDetailCheck(userToken, postId, memberId)
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
                        1010, 1011, 1012, 1013 -> getPostView.onGetPostSuccess(
                            resp.code,
                            resp.result
                        )
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
    private fun setPost(code: Int, result: PostDetailData) {
        // post 설정
        binding.vibePostPostTitleTv.text = result.title
        binding.vibePostPostBodyTv.text = result.body
        binding.vibePostNicknameTv.text = result.nickname
        if (!result.profileImg.isNullOrEmpty()) {
            Glide.with(applicationContext).load(result.profileImg).circleCrop()
                .into(binding.vibePostProfileIv)
        }
        // modifiedDate 설정
        var modifiedDate = result.modifiedDate.replace("-", ". ").replace("T", ". ")
        val dateLastIdx = modifiedDate.lastIndexOf(":")
        modifiedDate = modifiedDate.removeRange(dateLastIdx, modifiedDate.length)
        binding.vibePostDateTv.text = modifiedDate

        // tag name 설정
        if (result.tagName.isNullOrEmpty()) {
            binding.vibePostTagLayout.visibility = View.GONE
        } else {
            Log.d("Tag set","${result.tagName}")
            // tag name 을 공백으로 구분
            val tagList = result.tagName.split(buildString {
                append("\\s")
            }.toRegex()).toMutableList()
            tagList.reverse()

            /*Log.d("Tag List","$tagList")*/
            // tag name 앞에 # 붙여주기
            tagList[0] = "#" + tagList[0]
            if(tagList[0].contains("\"")) {
                tagList[0] = tagList[0].substring(0, tagList[0].length - 1)
            }
          /*  Log.d("TagWoo", "$tagList")*/
            if(tagList[1]=="" || tagList[1]==""+"#"){
                tagList.removeAt(1)
                Log.d("Tag","All claer")
            }else if(!tagList[1].contains('#')){
                tagList[1] = "#" + tagList[1]
            }
           /* Log.d("TagWoo2", "$tagList")
            Log.d("TagWoo3", "${tagList.size}")*/
            // tag name 최대 6개라고 가정하고 View visibility 설정
            binding.vibePostTagLayout.visibility = View.VISIBLE
            when (tagList.size) {
                1 -> {
                    binding.vibePostTagFirstTv.visibility = View.VISIBLE
                    binding.vibePostTagFirstTv.text = tagList[0]
                    binding.vibePostTagSecondTv.visibility = View.GONE
                    binding.vibePostTagThirdTv.visibility = View.GONE
                    binding.vibePostTagFourthTv.visibility = View.GONE
                    binding.vibePostTagFifthTv.visibility = View.GONE
                    binding.vibePostTagLastTv.visibility = View.GONE
                }
                2 -> {
                    binding.vibePostTagFirstTv.visibility = View.VISIBLE
                    binding.vibePostTagFirstTv.text = tagList[0]
                    binding.vibePostTagSecondTv.visibility = View.VISIBLE
                    binding.vibePostTagSecondTv.text = tagList[1]
                    binding.vibePostTagThirdTv.visibility = View.GONE
                    binding.vibePostTagFourthTv.visibility = View.GONE
                    binding.vibePostTagFifthTv.visibility = View.GONE
                    binding.vibePostTagLastTv.visibility = View.GONE
                }
                3 -> {
                    binding.vibePostTagFirstTv.visibility = View.VISIBLE
                    binding.vibePostTagFirstTv.text = tagList[0]
                    binding.vibePostTagSecondTv.visibility = View.VISIBLE
                    binding.vibePostTagSecondTv.text = tagList[1]
                    binding.vibePostTagThirdTv.visibility = View.VISIBLE
                    binding.vibePostTagThirdTv.text = tagList[2]
                    binding.vibePostTagFourthTv.visibility = View.GONE
                    binding.vibePostTagFifthTv.visibility = View.GONE
                    binding.vibePostTagLastTv.visibility = View.GONE
                }
                4 -> {
                    binding.vibePostTagFirstTv.visibility = View.VISIBLE
                    binding.vibePostTagFirstTv.text = tagList[0]
                    binding.vibePostTagSecondTv.visibility = View.VISIBLE
                    binding.vibePostTagSecondTv.text = tagList[1]
                    binding.vibePostTagThirdTv.visibility = View.VISIBLE
                    binding.vibePostTagThirdTv.text = tagList[2]
                    binding.vibePostTagFourthTv.visibility = View.VISIBLE
                    binding.vibePostTagFourthTv.text = tagList[3]
                    binding.vibePostTagFifthTv.visibility = View.GONE
                    binding.vibePostTagLastTv.visibility = View.GONE
                }
                5 -> {
                    binding.vibePostTagFirstTv.visibility = View.VISIBLE
                    binding.vibePostTagFirstTv.text = tagList[0]
                    binding.vibePostTagSecondTv.visibility = View.VISIBLE
                    binding.vibePostTagSecondTv.text = tagList[1]
                    binding.vibePostTagThirdTv.visibility = View.VISIBLE
                    binding.vibePostTagThirdTv.text = tagList[2]
                    binding.vibePostTagFourthTv.visibility = View.VISIBLE
                    binding.vibePostTagFourthTv.text = tagList[3]
                    binding.vibePostTagFifthTv.visibility = View.VISIBLE
                    binding.vibePostTagFifthTv.text = tagList[4]
                    binding.vibePostTagLastTv.visibility = View.GONE
                }
                6 -> {
                    binding.vibePostTagFirstTv.visibility = View.VISIBLE
                    binding.vibePostTagFirstTv.text = tagList[0]
                    binding.vibePostTagSecondTv.visibility = View.VISIBLE
                    binding.vibePostTagSecondTv.text = tagList[1]
                    binding.vibePostTagThirdTv.visibility = View.VISIBLE
                    binding.vibePostTagThirdTv.text = tagList[2]
                    binding.vibePostTagFourthTv.visibility = View.VISIBLE
                    binding.vibePostTagFourthTv.text = tagList[3]
                    binding.vibePostTagFifthTv.visibility = View.VISIBLE
                    binding.vibePostTagFifthTv.text = tagList[4]
                    binding.vibePostTagLastTv.visibility = View.VISIBLE
                    binding.vibePostTagFirstTv.text = tagList[5]
                }
            }

        }

        // youtube link 설정
        fullvideoId=result.youtubeLink
        val beginIdx = result.youtubeLink.indexOf("watch?v=")
        val endIdx = result.youtubeLink.length
        val videoId = result.youtubeLink.substring(beginIdx + 8, endIdx)

        val youTubePlayerView: YouTubePlayerView =binding.vibePostYoutubePlayerView
       // lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo((videoId), 0F)
            }
        })
        youTubePlayerView.enableBackgroundPlayback(true)

        binding.vibePostLikeCountTv.text = result.likeNumber.toString()
        binding.vibePostCommentCountTv.text = result.commentNumber.toString()

        // 좋아요, 스크랩 여부 설정
        when (code) {
            1010 -> {
                // 좋아요 O, 스크랩 O 게시물
                binding.vibePostLikeBtn.setImageResource(R.drawable.ic_activity_vibe_post_like_on) // 임시로 아무 사진
                binding.vibePostScrapBtn.setImageResource(R.drawable.ic_activity_vibe_post_save_on) // 임시로 아무 사진
            }
            1011 -> {
                // 좋아요 X, 스크랩 X 게시물
                binding.vibePostLikeBtn.setImageResource(R.drawable.ic_activity_vibe_post_heart) // 임시로 아무 사진
                binding.vibePostScrapBtn.setImageResource(R.drawable.ic_activity_vibe_post_save) // 임시로 아무 사진
            }
            1012 -> {
                // 좋아요 O, 스크랩 X 게시물
                binding.vibePostLikeBtn.setImageResource(R.drawable.ic_activity_vibe_post_like_on) // 임시로 아무 사진
                binding.vibePostScrapBtn.setImageResource(R.drawable.ic_activity_vibe_post_save)
            }
            1013 -> {
                // 좋아요 X, 스크랩 O 게시물
                binding.vibePostLikeBtn.setImageResource(R.drawable.ic_activity_vibe_post_heart)
                binding.vibePostScrapBtn.setImageResource(R.drawable.ic_activity_vibe_post_save_on) // 임시로 아무 사진
            }
        }

    }

    /*** 게시물 좋아요 ***/
    private fun setLike(userToken: String, postId: Int, memberId: MemberId) {
        val vibePostService = getRetrofit().create(VibePostApiInterface::class.java)
        vibePostService.postLike(userToken, postId, memberId)
            .enqueue(object : Callback<PostLikeResponse> {
                override fun onResponse(
                    call: Call<PostLikeResponse>,
                    response: Response<PostLikeResponse>
                ) {
                    Log.d("[VIBE] SET_LIKE/SUCCESS", response.toString())
                    val resp: PostLikeResponse = response.body()!!

                    Log.d("[VIBE] SET_LIKE/CODE", resp.code.toString())

                    // 서버 response 중 code 값에 따른 결과
                    when (resp.code) {
                        1000 -> setLikeView.onSetLikeSuccess(resp.result)
                        else -> setLikeView.onSetLikeFailure(
                            resp.code,
                            resp.message
                        )
                    }
                }

                override fun onFailure(call: Call<PostLikeResponse>, t: Throwable) {
                    Log.d("[VIBE] SET_LIKE/FAILURE", t.message.toString())
                }
            })
        Log.d("[VIBE] SET_LIKE", "HELLO")
    }

    /*** 게시물 스크랩 ***/
    private fun setScrap(userToken: String, postId: Int, memberId: MemberId) {
        val vibePostService = getRetrofit().create(VibePostApiInterface::class.java)
        vibePostService.postScrap(userToken, postId, memberId)
            .enqueue(object : Callback<PostScrapResponse> {
                override fun onResponse(
                    call: Call<PostScrapResponse>,
                    response: Response<PostScrapResponse>
                ) {
                    Log.d("[VIBE] SET_SCRAP/SUCCESS", response.toString())
                    val resp: PostScrapResponse = response.body()!!

                    Log.d("[VIBE] SET_SCRAP/CODE", resp.code.toString())

                    // 서버 response 중 code 값에 따른 결과
                    when (resp.code) {
                        1000 -> setScrapView.onSetScrapSuccess(resp.result)
                        else -> setScrapView.onSetScrapFailure(resp.code, resp.message)
                    }
                }

                override fun onFailure(call: Call<PostScrapResponse>, t: Throwable) {
                    Log.d("[VIBE] SET_SCRAP/FAILURE", t.message.toString())
                }
            })
        Log.d("[VIBE] SET_SCRAP", "HELLO")
    }

    // 게시물 메뉴 BottomSheet Click event 설정

    private fun setPostBottomSheetView(
        bottomSheetView: View,
        dialog: BottomSheetDialog,
        context: Context
    ) {

        //`수정`하기
        val editBlockBtn =
        bottomSheetView.findViewById<ConstraintLayout>(R.id.bottom_sheet_mypage_post_edit)
        editBlockBtn.setOnClickListener {
            val vibeId=intent.extras!!.getInt("vibe_id")
            val intent = Intent(this,CommonEditActivity::class.java)
            intent.putExtra("post_id",postId)
            intent.putExtra("vibe_id",vibeId)
            if(postMenuBottomSheetDialog!=null&&postMenuBottomSheetDialog!!.isShowing){
                postMenuBottomSheetDialog!!.dismiss()
            }
            startActivity(intent)
            //this@MypagePostActivity.finish()
        }
        //삭제하기
        val deleteBlockBtn=
            bottomSheetView.findViewById<ConstraintLayout>(R.id.bottom_sheet_mypage_post_delete)
        deleteBlockBtn.setOnClickListener{
            val postId = intent.getIntExtra("post_id", 0)
            val vibeId = intent.extras!!.getInt("vibe_id")
            var member_id = MEMBER_ID.toInt()

            Log.d("postId","${postId}")
            Log.d("vibeId","${vibeId}")

            val apiService = retrofit.create(MypageApiInterface::class.java)
            apiService.deleteMypagePost(postId,userToken, deleteMypagePostInput(member_id)).enqueue(object :
                    Callback<deleteMypageResponse> {
                    override fun onResponse(
                        call: Call<deleteMypageResponse>,
                        response: Response<deleteMypageResponse>
                    ) {

                        if (response.isSuccessful) {
                            val responseData = response.body()

                            if (responseData !== null) {
                                Log.d(
                                    "Retrofit",
                                    "MypageNicknameResponse\n"+
                                            "isSuccess:${responseData.is_success}" +
                                            "Code:${responseData.code}"+
                                            "Message:${responseData.message}"+
                                            "Result:${responseData.result}"
                                )
                                if (responseData?.is_success==true) {
                                    when(response.body()?.code){
                                        1000 ->{
                                            Toast.makeText(
                                                applicationContext,
                                                "게시글이 삭제되었습니다",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                           val nextIntent = Intent(this@MypagePostActivity, MypageWritedActivity::class.java)
                                            startActivity(nextIntent)
                                            this@MypagePostActivity.finish()
                                        }
                                        500 -> {
                                            Log.d ("레트로핏","해당 바이브에 대한 접근 권한이 없습니다" )
                                        }
                                    }
                                }
                                else {
                                    Log.d("레트로핏","Response Not Success ${response.code()}")
                                }
                            }
                            else{
                                Log.d("Retrofit","Null data") }

                        } else {
                            Log.w("Retrofit", "Response Not Successful${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<deleteMypageResponse>, t: Throwable) {
                        Log.e("Retrofit","Error",t)
                    }

                })

        }

        //공유하기
        val shareBlockBtn=
            bottomSheetView.findViewById<ConstraintLayout>(R.id.bottom_sheet_mypage_post_share)
        shareBlockBtn.setOnClickListener(){
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, fullvideoId)
            }
            startActivity(Intent.createChooser(intent, fullvideoId))
        }

        // 상단 close bar 버튼 누르면 닫기
        val closeBtn =
            bottomSheetView.findViewById<ImageButton>(R.id.bottom_sheet_vibe_post_menu_close_btn)
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }
    }

    // 게시물 신고 메뉴 BottomSheet Click event 설정
    private fun setPostReportBottomSheetView(
        bottomSheetView: View,
        dialog: BottomSheetDialog,
        context: Context
    ) {
    }

    private fun setPostView(
        getPostView: GetPostView,
        setLikeView: SetLikeView,
        setScrapView: SetScrapView
    ) {
        this.getPostView = getPostView
        this.setLikeView = setLikeView
        this.setScrapView = setScrapView
    }

    /**
     * 게시물 1개 조회 성공, 실패 처리
     */
    override fun onGetPostSuccess(code: Int, result: PostDetailData) {
        setPost(code, result)
    }

    override fun onGetPostFailure(code: Int, message: String) {
        Log.d("[VIBE] GET_POST/FAILURE", "$code / $message")
    }

    /**
     * 게시물 좋아요 성공, 실패 처리
     */
    override fun onSetLikeSuccess(result: IsPostLiked) {
        val str = "해당 게시물에 좋아요를 눌렀습니다."
        if (result.likeOrElse == str) {
            (binding.vibePostLikeCountTv.text.toString().toInt() + 1).toString()
                .also { binding.vibePostLikeCountTv.text = it }
            binding.vibePostLikeBtn.setImageResource(R.drawable.ic_activity_vibe_post_like_on) // 임시로 아무 사진
        } else {
            (binding.vibePostLikeCountTv.text.toString().toInt() - 1).toString()
                .also { binding.vibePostLikeCountTv.text = it }
            binding.vibePostLikeBtn.setImageResource(R.drawable.ic_activity_vibe_post_heart)
        }
    }

    override fun onSetLikeFailure(code: Int, message: String) {
        Log.d("[VIBE] SET_LIKE/FAILURE", "$code / $message")
    }

    /**
     * 게시물 스크랩 성공, 실패 처리
     */
    override fun onSetScrapSuccess(result: IsPostScraped) {
        val str = "해당 게시물을 스크랩하였습니다."
        if (result.scrapOrElse == str) {
            binding.vibePostScrapBtn.setImageResource(R.drawable.ic_activity_vibe_post_save_on) // 임시로 아무 사진
        } else {
            binding.vibePostScrapBtn.setImageResource(R.drawable.ic_activity_vibe_post_save)
        }
    }

    override fun onSetScrapFailure(code: Int, message: String) {
        Log.d("[VIBE] SET_SCRAP/FAILURE", "$code / $message")
    }

    override fun onRestart() {
        super.onRestart()
        setPostView(this, this, this)


        getPost(postId!!, MEMBER_ID)
    }



}
