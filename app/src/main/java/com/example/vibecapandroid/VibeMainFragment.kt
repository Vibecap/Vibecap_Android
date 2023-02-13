package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.vibecapandroid.R.id.*
import com.example.vibecapandroid.coms.*
import com.example.vibecapandroid.databinding.FragmentVibeMainBinding
import com.example.vibecapandroid.utils.getRetrofit
import kotlinx.coroutines.*
import me.relex.circleindicator.CircleIndicator3
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class VibeMainFragment : Fragment() {
    private lateinit var viewBinding: FragmentVibeMainBinding

    private lateinit var mMapLayoutManager: StaggeredGridLayoutManager
    private lateinit var mListAdapter: VibeMainAllPostsRVAdapter

    private var totalCount = 0 // 전체 아이템 개수
    private var isNext = false // 다음 페이지 유무
    private var page = -1      // 현재 페이지
    private var limit = 8     // 한 번에 가져올 아이템 수

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentVibeMainBinding.inflate(layoutInflater)

        // 상태바 설정
        val windowController = WindowInsetsControllerCompat(
            requireActivity().window,
            requireActivity().window.decorView
        )
        windowController.isAppearanceLightStatusBars = true
        viewBinding.vibeMainLayout.setPadding(0, requireContext().statusBarHeight(), 0, 0)


        // 게시물 전체 조회 (tag X)
        mListAdapter = VibeMainAllPostsRVAdapter(requireContext())
        mMapLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        viewBinding.vibeMainAllPostsRv.layoutManager = mMapLayoutManager
        viewBinding.vibeMainAllPostsRv.adapter = mListAdapter

        mListAdapter.setMyItemClickListener(object :
            VibeMainAllPostsRVAdapter.MyItemClickListener {
            override fun onItemClick(postContentData: PostContentData) {
                val intent = Intent(context, VibePostActivity::class.java)
                intent.putExtra("post_id", postContentData.post_id)
                startActivity(intent)
            }
        })

        getAllPosts()
        initScrollListener()


        val view = inflater.inflate(R.layout.fragment_vibe_main, container, false)

        val tag1 = viewBinding.tvTag1
        val tag2 = viewBinding.tvTag2
        val tag3 = viewBinding.tvTag3
        val tag4 = viewBinding.tvTag4
        val tag5 = viewBinding.tvTag5
        val tag6 = viewBinding.tvTag6
        val tag7 = viewBinding.tvTag7
        val tag8 = viewBinding.tvTag8
        val addView = viewBinding.btnAddview


        //  post 테스트 용************
        val testPost: ImageView = view.findViewById(R.id.imageButton_1)
        testPost.setOnClickListener {
            val intent = Intent(context, VibePostActivity::class.java)
            intent.putExtra("post_id", 32)
            startActivity(intent)
        }
        //**********


        val search = viewBinding.imageButtonSearch
        search.setOnClickListener {
            val intent = Intent(context, VibeSearchActivity::class.java)
            startActivity(intent)
        }

        val mypage_alarm2 = viewBinding.imageButtonAlarm
        mypage_alarm2.setOnClickListener {
            val intent = Intent(context, MypageAlarmActivity::class.java)
            startActivity(intent)
        }

        val mypage_profile2 = viewBinding.imageButtonProfile
        mypage_profile2.setOnClickListener {
            val intent = Intent(context, MypageProfileActivity::class.java)
            startActivity(intent)
        }


        // 태그 게시물 더보기
        addView.setOnClickListener {
            val index: Int = addView.text.indexOf(" ")
            val addViewText: String = addView.text.substring(0, index)

            val intent = Intent(context, VibeDetailActivity::class.java)
            intent.putExtra("add_view_text", addViewText)
            startActivity(intent)
        }

        defaultTag()
        tag1.setOnClickListener {
            tag1.setTextColor(Color.BLACK)
            tag2.setTextColor(Color.GRAY)
            tag3.setTextColor(Color.GRAY)
            tag4.setTextColor(Color.GRAY)
            tag5.setTextColor(Color.GRAY)
            tag6.setTextColor(Color.GRAY)
            tag7.setTextColor(Color.GRAY)
            tag8.setTextColor(Color.GRAY)

            addView.text = "#신나는 더보기"
            //api
            callTagAPI("신나는")
        }

        tag2.setOnClickListener {
            tag1.setTextColor(Color.GRAY)
            tag2.setTextColor(Color.BLACK)
            tag3.setTextColor(Color.GRAY)
            tag4.setTextColor(Color.GRAY)
            tag5.setTextColor(Color.GRAY)
            tag6.setTextColor(Color.GRAY)
            tag7.setTextColor(Color.GRAY)
            tag8.setTextColor(Color.GRAY)

            addView.text = "#포근한 더보기"
            //api
            callTagAPI("포근한")
        }

        tag3.setOnClickListener {
            tag1.setTextColor(Color.GRAY)
            tag2.setTextColor(Color.GRAY)
            tag3.setTextColor(Color.BLACK)
            tag4.setTextColor(Color.GRAY)
            tag5.setTextColor(Color.GRAY)
            tag6.setTextColor(Color.GRAY)
            tag7.setTextColor(Color.GRAY)
            tag8.setTextColor(Color.GRAY)

            addView.text = "#선선한 더보기"
            //api
            callTagAPI("선선한")
        }

        tag4.setOnClickListener {
            tag1.setTextColor(Color.GRAY)
            tag2.setTextColor(Color.GRAY)
            tag3.setTextColor(Color.GRAY)
            tag4.setTextColor(Color.BLACK)
            tag5.setTextColor(Color.GRAY)
            tag6.setTextColor(Color.GRAY)
            tag7.setTextColor(Color.GRAY)
            tag8.setTextColor(Color.GRAY)

            addView.text = "#낭만적인 더보기"
            //api
            callTagAPI("낭만적인")
        }

        tag5.setOnClickListener {
            tag1.setTextColor(Color.GRAY)
            tag2.setTextColor(Color.GRAY)
            tag3.setTextColor(Color.GRAY)
            tag4.setTextColor(Color.GRAY)
            tag5.setTextColor(Color.BLACK)
            tag6.setTextColor(Color.GRAY)
            tag7.setTextColor(Color.GRAY)
            tag8.setTextColor(Color.GRAY)

            addView.text = "#잔잔한 더보기"
            //api
            callTagAPI("잔잔한")
        }

        tag6.setOnClickListener {
            tag1.setTextColor(Color.GRAY)
            tag2.setTextColor(Color.GRAY)
            tag3.setTextColor(Color.GRAY)
            tag4.setTextColor(Color.GRAY)
            tag5.setTextColor(Color.GRAY)
            tag6.setTextColor(Color.BLACK)
            tag7.setTextColor(Color.GRAY)
            tag8.setTextColor(Color.GRAY)

            addView.text = "#우울한 더보기"
            //api
            callTagAPI("우울한")
        }

        tag7.setOnClickListener {
            tag1.setTextColor(Color.GRAY)
            tag2.setTextColor(Color.GRAY)
            tag3.setTextColor(Color.GRAY)
            tag4.setTextColor(Color.GRAY)
            tag5.setTextColor(Color.GRAY)
            tag6.setTextColor(Color.GRAY)
            tag7.setTextColor(Color.BLACK)
            tag8.setTextColor(Color.GRAY)

            addView.text = "#공허한 더보기"
            //api
            callTagAPI("공허한")
        }

        tag8.setOnClickListener {
            tag1.setTextColor(Color.GRAY)
            tag2.setTextColor(Color.GRAY)
            tag3.setTextColor(Color.GRAY)
            tag4.setTextColor(Color.GRAY)
            tag5.setTextColor(Color.GRAY)
            tag6.setTextColor(Color.GRAY)
            tag7.setTextColor(Color.GRAY)
            tag8.setTextColor(Color.BLACK)

            addView.text = "#심심한 더보기"
            //api
            callTagAPI("심심한")
        }
        requestWeeklyAPI()
        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()

        page = -1
        getAllPosts()
        initScrollListener()
    }


    private fun defaultTag() {
        val tag1 = viewBinding.tvTag1
        val tag2 = viewBinding.tvTag2
        val tag3 = viewBinding.tvTag3
        val tag4 = viewBinding.tvTag4
        val tag5 = viewBinding.tvTag5
        val tag6 = viewBinding.tvTag6
        val tag7 = viewBinding.tvTag7
        val tag8 = viewBinding.tvTag8
        val addView = viewBinding.btnAddview

        tag1.setTextColor(Color.BLACK)
        tag2.setTextColor(Color.GRAY)
        tag3.setTextColor(Color.GRAY)
        tag4.setTextColor(Color.GRAY)
        tag5.setTextColor(Color.GRAY)
        tag6.setTextColor(Color.GRAY)
        tag7.setTextColor(Color.GRAY)
        tag8.setTextColor(Color.GRAY)

        addView.text = "#신나는 더보기"
        //api
        callTagAPI("신나는")
    }

    // baseUrl api
    object TagRetrofitObject {
        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getApiService(): VibePostTagInterface {
            return getRetrofit().create(VibePostTagInterface::class.java)
        }
    }

    object WeeklyRetrofitObject {
        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getApiService(): VibePostWeeklyInterface {
            return getRetrofit().create(VibePostWeeklyInterface::class.java)
        }
    }

    // WeeklyAPI
    private fun requestWeeklyAPI() {
        WeeklyRetrofitObject.getApiService().postWeeklyCheck().enqueue(object :
            Callback<PostWeeklyResponse> {
            // api 호출 성공시
            override fun onResponse(
                call: Call<PostWeeklyResponse>, response: Response<PostWeeklyResponse>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        Log.d(
                            "WeeklyResult",
                            "WeeklyResult\n" +
                                    "isSuccess:${responseData?.is_success}\n " +
                                    "Code: ${responseData?.code} \n" +
                                    "Message:${responseData?.message} \n" +
                                    "result:${responseData.result}\n"
                        )

                        if (responseData.is_success) {
                            when (response.body()?.code) {
                                1000 -> {

                                    // viewPager
                                    val weeklyarray = responseData.result
                                    viewBinding.ViewPagerBanner.adapter = ViewPagerAdapter(requireContext(),weeklyarray.toTypedArray()) // 어댑터 생성
                                    viewBinding.ViewPagerBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로

                                    // 인디케이터 적용
                                    val viewpager: ViewPager2 = view!!.findViewById(ViewPager_banner)
                                    viewpager.adapter = ViewPagerAdapter(requireContext(),weeklyarray.toTypedArray())
                                    val indicator: CircleIndicator3 = view!!.findViewById(dots_indicator)
                                    indicator.setViewPager(viewpager)
                                }
                            }
                        } else {
                            if (responseData.code == 3011) {
                                //Toast.makeText(this@VibeMainFragment,"해당 태그를 가진 게시물이 없습니다.", Toast.LENGTH_SHORT).show()
                                Log.d("WeeklyResult","WeeklyResult 실패")
                            }
                        }
                    }
                }

            }

            // api 호출 실패시
            override fun onFailure(call: Call<PostWeeklyResponse>, t: Throwable) {
                Log.e("retrofit onFailure", "${t.message.toString()}")
            }
        })
    }



    // 태그별 API
    private fun callTagAPI(tagName: String) {
        TagRetrofitObject.getApiService().postAllCheck(tagName).enqueue(object :
            Callback<PostTagResponse> {
            // api 호출 성공시
            override fun onResponse(
                call: Call<PostTagResponse>,
                response: Response<PostTagResponse>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        Log.d(
                            "TagResult",
                            "TagResult\n" +
                                    "isSuccess:${responseData?.is_success}\n " +
                                    "Code: ${responseData?.code} \n" +
                                    "Message:${responseData?.message} \n"
                        )
                        if (responseData.is_success) {
                            when (response.body()?.code) {
                                1000 -> {
                                    // 데이터 저장하기
                                    viewBinding.tagAlert.visibility = View.GONE
                                    viewBinding.tableLayoutMain.visibility = View.VISIBLE
                                    TagsaveData(tagName, responseData)

                                }
                                3011 -> {
                                    viewBinding.tagAlert.visibility = View.VISIBLE
                                    viewBinding.tableLayoutMain.visibility = View.GONE
                                }
                            }
                        } else {
                            if (responseData.code == 3011) {
                                viewBinding.tagAlert.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }

            // api 호출 실패시
            override fun onFailure(call: Call<PostTagResponse>, t: Throwable) {
                Log.e("retrofit Tag onFailure2", "${t.message.toString()}")
            }
        })
    }


    // 태그별 저장
    private fun TagsaveData(tagName: String, responseData: PostTagResponse) {
        viewBinding.imageButton1.visibility = View.VISIBLE
        viewBinding.imageButton2.visibility = View.VISIBLE
        viewBinding.imageButton3.visibility = View.VISIBLE
        viewBinding.imageButton4.visibility = View.VISIBLE
        viewBinding.imageButton5.visibility = View.VISIBLE
        viewBinding.imageButton6.visibility = View.VISIBLE

        if (tagName.isNullOrEmpty()) {
            Log.d("tagEmpty", "태그 이름 없음")
        } else {
            val defaultImage = null
            when(responseData.result.content.size){
                0 -> {
                    Log.d("tagEmpty", "태그 게시물 없음")
                }
                1 -> {
                    // 1번째 post
                    val post_id_1 = responseData.result.content[0].post_id
                    //val member_id_1 = responseData.result.content[0].member_id
                    //val vibe_id_1 = responseData.result.content[0].vibe_id
                    val vibe_image_1 = responseData.result.content[0].vibe_image

                    val imageView_1 = requireView().findViewById<ImageView>(R.id.imageButton_1)
                    imageView_1.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_1) // 불러올 이미지 url
                        .fitCenter()
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                       // .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_1) // 이미지를 넣을 뷰

                    // post id 설정
                    viewBinding.imageButton1.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_1)
                        startActivity(intent)
                    }

                    viewBinding.imageButton2.visibility = View.GONE
                    viewBinding.imageButton3.visibility = View.GONE
                    viewBinding.imageButton4.visibility = View.GONE
                    viewBinding.imageButton5.visibility = View.GONE
                    viewBinding.imageButton6.visibility = View.GONE
                }
                2 -> {
                    // 1번째 post
                    val post_id_1 = responseData.result.content[0].post_id
                    //val member_id_1 = responseData.result.content[0].member_id
                    //val vibe_id_1 = responseData.result.content[0].vibe_id
                    val vibe_image_1 = responseData.result.content[0].vibe_image

                    val imageView_1 = requireView().findViewById<ImageView>(R.id.imageButton_1)
                    imageView_1.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_1) // 불러올 이미지 url
                        .fitCenter()
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                       // .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_1) // 이미지를 넣을 뷰

                    // 2번째 post
                    val post_id_2 = responseData.result.content[1].post_id
                    //val member_id_2 = responseData.result.content[1].member_id
                    //val vibe_id_2 = responseData.result.content[1].vibe_id
                    val vibe_image_2 = responseData.result.content[1].vibe_image

                    val imageView_2 = requireView().findViewById<ImageView>(R.id.imageButton_2)
                    imageView_2.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_2) // 불러올 이미지 url
                        .fitCenter()
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                      //  .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_2) // 이미지를 넣을 뷰

                    viewBinding.imageButton3.visibility = View.GONE
                    viewBinding.imageButton4.visibility = View.GONE
                    viewBinding.imageButton5.visibility = View.GONE
                    viewBinding.imageButton6.visibility = View.GONE

                    // post id 설정
                    viewBinding.imageButton1.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_1)
                        startActivity(intent)
                    }
                    viewBinding.imageButton2.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_2)
                        startActivity(intent)
                    }
                }
                3 -> {
                    // 1번째 post
                    val post_id_1 = responseData.result.content[0].post_id
                    //val member_id_1 = responseData.result.content[0].member_id
                    //val vibe_id_1 = responseData.result.content[0].vibe_id
                    val vibe_image_1 = responseData.result.content[0].vibe_image

                    val imageView_1 = requireView().findViewById<ImageView>(R.id.imageButton_1)
                    imageView_1.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_1) // 불러올 이미지 url
                        .fitCenter()
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                     //   .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_1) // 이미지를 넣을 뷰

                    // 2번째 post
                    val post_id_2 = responseData.result.content[1].post_id
                    //val member_id_2 = responseData.result.content[1].member_id
                    //val vibe_id_2 = responseData.result.content[1].vibe_id
                    val vibe_image_2 = responseData.result.content[1].vibe_image

                    val imageView_2 = requireView().findViewById<ImageView>(R.id.imageButton_2)
                    imageView_2.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_2) // 불러올 이미지 url
                        .fitCenter()
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                     //   .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_2) // 이미지를 넣을 뷰

                    // 3번째 post
                    val post_id_3 = responseData.result.content[2].post_id
                    //val member_id_3 = responseData.result.content[2].member_id
                    //val vibe_id_3 = responseData.result.content[2].vibe_id
                    val vibe_image_3 = responseData.result.content[2].vibe_image

                    val imageView_3 = requireView().findViewById<ImageView>(R.id.imageButton_3)
                    imageView_3.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_3) // 불러올 이미지 url
                        .override(800, 200)
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                      //  .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_3) // 이미지를 넣을 뷰

                    viewBinding.imageButton4.visibility = View.GONE
                    viewBinding.imageButton5.visibility = View.GONE
                    viewBinding.imageButton6.visibility = View.GONE

                    // post id 설정
                    viewBinding.imageButton1.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_1)
                        startActivity(intent)
                    }
                    viewBinding.imageButton2.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_2)
                        startActivity(intent)
                    }
                    viewBinding.imageButton3.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_3)
                        startActivity(intent)
                    }
                }
                4 -> {
                    // 1번째 post
                    val post_id_1 = responseData.result.content[0].post_id
                    //val member_id_1 = responseData.result.content[0].member_id
                    //val vibe_id_1 = responseData.result.content[0].vibe_id
                    val vibe_image_1 = responseData.result.content[0].vibe_image

                    val imageView_1 = requireView().findViewById<ImageView>(R.id.imageButton_1)
                    imageView_1.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_1) // 불러올 이미지 url
                        .fitCenter()
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                        //.error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_1) // 이미지를 넣을 뷰

                    // 2번째 post
                    val post_id_2 = responseData.result.content[1].post_id
                    //val member_id_2 = responseData.result.content[1].member_id
                    //val vibe_id_2 = responseData.result.content[1].vibe_id
                    val vibe_image_2 = responseData.result.content[1].vibe_image

                    val imageView_2 = requireView().findViewById<ImageView>(R.id.imageButton_2)
                    imageView_2.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_2) // 불러올 이미지 url
                        .fitCenter()
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                     //   .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_2) // 이미지를 넣을 뷰

                    // 3번째 post
                    val post_id_3 = responseData.result.content[2].post_id
                    //val member_id_3 = responseData.result.content[2].member_id
                    //val vibe_id_3 = responseData.result.content[2].vibe_id
                    val vibe_image_3 = responseData.result.content[2].vibe_image

                    val imageView_3 = requireView().findViewById<ImageView>(R.id.imageButton_3)
                    imageView_3.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_3) // 불러올 이미지 url
                        .fitCenter()
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                     //   .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_3) // 이미지를 넣을 뷰

                    // 4번째 post
                    val post_id_4 = responseData.result.content[3].post_id
                    // val member_id_4 = responseData.result.content[3].member_id
                    //val vibe_id_4 = responseData.result.content[3].vibe_id
                    val vibe_image_4 = responseData.result.content[3].vibe_image

                    val imageView_4 = requireView().findViewById<ImageView>(R.id.imageButton_4)
                    imageView_4.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_4) // 불러올 이미지 url
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                     //   .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_4) // 이미지를 넣을 뷰

                    viewBinding.imageButton5.visibility = View.GONE
                    viewBinding.imageButton6.visibility = View.GONE

                    // post id 설정
                    viewBinding.imageButton1.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_1)
                        startActivity(intent)
                    }
                    viewBinding.imageButton2.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_2)
                        startActivity(intent)
                    }
                    viewBinding.imageButton3.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_3)
                        startActivity(intent)
                    }
                    viewBinding.imageButton4.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_4)
                        startActivity(intent)
                    }
                }
                5 -> {
                    // 1번째 post
                    val post_id_1 = responseData.result.content[0].post_id
                    //val member_id_1 = responseData.result.content[0].member_id
                    //val vibe_id_1 = responseData.result.content[0].vibe_id
                    val vibe_image_1 = responseData.result.content[0].vibe_image

                    val imageView_1 = requireView().findViewById<ImageView>(R.id.imageButton_1)
                    imageView_1.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_1) // 불러올 이미지 url
                        .fitCenter()
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                      //  .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_1) // 이미지를 넣을 뷰

                    // 2번째 post
                    val post_id_2 = responseData.result.content[1].post_id
                    //val member_id_2 = responseData.result.content[1].member_id
                    //val vibe_id_2 = responseData.result.content[1].vibe_id
                    val vibe_image_2 = responseData.result.content[1].vibe_image

                    val imageView_2 = requireView().findViewById<ImageView>(R.id.imageButton_2)
                    imageView_2.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_2) // 불러올 이미지 url
                        .fitCenter()
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                       // .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_2) // 이미지를 넣을 뷰

                    // 3번째 post
                    val post_id_3 = responseData.result.content[2].post_id
                    //val member_id_3 = responseData.result.content[2].member_id
                    //val vibe_id_3 = responseData.result.content[2].vibe_id
                    val vibe_image_3 = responseData.result.content[2].vibe_image

                    val imageView_3 = requireView().findViewById<ImageView>(R.id.imageButton_3)
                    imageView_3.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_3) // 불러올 이미지 url
                        .override(800, 200)
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                      //  .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_3) // 이미지를 넣을 뷰

                    // 4번째 post
                    val post_id_4 = responseData.result.content[3].post_id
                    // val member_id_4 = responseData.result.content[3].member_id
                    //val vibe_id_4 = responseData.result.content[3].vibe_id
                    val vibe_image_4 = responseData.result.content[3].vibe_image

                    val imageView_4 = requireView().findViewById<ImageView>(R.id.imageButton_4)
                    imageView_4.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_4) // 불러올 이미지 url
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                      //  .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_4) // 이미지를 넣을 뷰

                    // 5번째 post
                    val post_id_5 = responseData.result.content[4].post_id
                    //val member_id_5 = responseData.result.content[4].member_id
                    // val vibe_id_5 = responseData.result.content[4].vibe_id
                    val vibe_image_5 = responseData.result.content[4].vibe_image

                    val imageView_5 = requireView().findViewById<ImageView>(R.id.imageButton_5)
                    imageView_5.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_5) // 불러올 이미지 url
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                       // .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_5) // 이미지를 넣을 뷰

                    viewBinding.imageButton6.visibility = View.GONE

                    // post id 설정
                    viewBinding.imageButton1.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_1)
                        startActivity(intent)
                    }
                    viewBinding.imageButton2.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_2)
                        startActivity(intent)
                    }
                    viewBinding.imageButton3.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_3)
                        startActivity(intent)
                    }
                    viewBinding.imageButton4.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_4)
                        startActivity(intent)
                    }
                    viewBinding.imageButton5.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_5)
                        startActivity(intent)
                    }
                }
                6 -> {
                    // 1번째 post
                    val post_id_1 = responseData.result.content[0].post_id
                    //val member_id_1 = responseData.result.content[0].member_id
                    //val vibe_id_1 = responseData.result.content[0].vibe_id
                    val vibe_image_1 = responseData.result.content[0].vibe_image

                    val imageView_1 = requireView().findViewById<ImageView>(R.id.imageButton_1)
                    imageView_1.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_1) // 불러올 이미지 url
                        .fitCenter()
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                    //    .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_1) // 이미지를 넣을 뷰

                    // 2번째 post
                    val post_id_2 = responseData.result.content[1].post_id
                    //val member_id_2 = responseData.result.content[1].member_id
                    //val vibe_id_2 = responseData.result.content[1].vibe_id
                    val vibe_image_2 = responseData.result.content[1].vibe_image

                    val imageView_2 = requireView().findViewById<ImageView>(R.id.imageButton_2)
                    imageView_2.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_2) // 불러올 이미지 url
                        .fitCenter()
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                     //   .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_2) // 이미지를 넣을 뷰

                    // 3번째 post
                    val post_id_3 = responseData.result.content[2].post_id
                    //val member_id_3 = responseData.result.content[2].member_id
                    //val vibe_id_3 = responseData.result.content[2].vibe_id
                    val vibe_image_3 = responseData.result.content[2].vibe_image

                    val imageView_3 = requireView().findViewById<ImageView>(R.id.imageButton_3)
                    imageView_3.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_3) // 불러올 이미지 url
                        .override(800, 200)
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                     //   .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_3) // 이미지를 넣을 뷰

                    // 4번째 post
                    val post_id_4 = responseData.result.content[3].post_id
                    // val member_id_4 = responseData.result.content[3].member_id
                    //val vibe_id_4 = responseData.result.content[3].vibe_id
                    val vibe_image_4 = responseData.result.content[3].vibe_image

                    val imageView_4 = requireView().findViewById<ImageView>(R.id.imageButton_4)
                    imageView_4.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_4) // 불러올 이미지 url
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                  //      .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_4) // 이미지를 넣을 뷰

                    // 5번째 post
                    val post_id_5 = responseData.result.content[4].post_id
                    //val member_id_5 = responseData.result.content[4].member_id
                    // val vibe_id_5 = responseData.result.content[4].vibe_id
                    val vibe_image_5 = responseData.result.content[4].vibe_image

                    val imageView_5 = requireView().findViewById<ImageView>(R.id.imageButton_5)
                    imageView_5.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_5) // 불러올 이미지 url
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                    //    .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_5) // 이미지를 넣을 뷰

                    // 6번째 post
                    val post_id_6 = responseData.result.content[5].post_id
                    //val member_id_6 = responseData.result.content[5].member_id
                    //val vibe_id_6 = responseData.result.content[5].vibe_id
                    val vibe_image_6 = responseData.result.content[5].vibe_image

                    val imageView_6 = requireView().findViewById<ImageView>(R.id.imageButton_6)
                    imageView_6.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_6) // 불러올 이미지 url
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                     //   .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_6) // 이미지를 넣을 뷰

                    // post id 설정
                    viewBinding.imageButton1.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_1)
                        startActivity(intent)
                    }
                    viewBinding.imageButton2.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_2)
                        startActivity(intent)
                    }
                    viewBinding.imageButton3.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_3)
                        startActivity(intent)
                    }
                    viewBinding.imageButton4.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_4)
                        startActivity(intent)
                    }
                    viewBinding.imageButton5.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_5)
                        startActivity(intent)
                    }
                    viewBinding.imageButton6.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_6)
                        startActivity(intent)
                    }
                }
                else -> {
                    // 1번째 post
                    val post_id_1 = responseData.result.content[0].post_id
                    //val member_id_1 = responseData.result.content[0].member_id
                    //val vibe_id_1 = responseData.result.content[0].vibe_id
                    val vibe_image_1 = responseData.result.content[0].vibe_image

                    val imageView_1 = requireView().findViewById<ImageView>(R.id.imageButton_1)
                    imageView_1.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_1) // 불러올 이미지 url
                        .fitCenter()
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                     //   .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_1) // 이미지를 넣을 뷰

                    // 2번째 post
                    val post_id_2 = responseData.result.content[1].post_id
                    //val member_id_2 = responseData.result.content[1].member_id
                    //val vibe_id_2 = responseData.result.content[1].vibe_id
                    val vibe_image_2 = responseData.result.content[1].vibe_image

                    val imageView_2 = requireView().findViewById<ImageView>(R.id.imageButton_2)
                    imageView_2.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_2) // 불러올 이미지 url
                        .fitCenter()
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                    //    .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_2) // 이미지를 넣을 뷰

                    // 3번째 post
                    val post_id_3 = responseData.result.content[2].post_id
                    //val member_id_3 = responseData.result.content[2].member_id
                    //val vibe_id_3 = responseData.result.content[2].vibe_id
                    val vibe_image_3 = responseData.result.content[2].vibe_image

                    val imageView_3 = requireView().findViewById<ImageView>(R.id.imageButton_3)
                    imageView_3.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_3) // 불러올 이미지 url
                        .override(800, 200)
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                    //    .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_3) // 이미지를 넣을 뷰

                    // 4번째 post
                    val post_id_4 = responseData.result.content[3].post_id
                    // val member_id_4 = responseData.result.content[3].member_id
                    //val vibe_id_4 = responseData.result.content[3].vibe_id
                    val vibe_image_4 = responseData.result.content[3].vibe_image

                    val imageView_4 = requireView().findViewById<ImageView>(R.id.imageButton_4)
                    imageView_4.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_4) // 불러올 이미지 url
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                    //    .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_4) // 이미지를 넣을 뷰

                    // 5번째 post
                    val post_id_5 = responseData.result.content[4].post_id
                    //val member_id_5 = responseData.result.content[4].member_id
                    // val vibe_id_5 = responseData.result.content[4].vibe_id
                    val vibe_image_5 = responseData.result.content[4].vibe_image

                    val imageView_5 = requireView().findViewById<ImageView>(R.id.imageButton_5)
                    imageView_5.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_5) // 불러올 이미지 url
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                    //    .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_5) // 이미지를 넣을 뷰

                    // 6번째 post
                    val post_id_6 = responseData.result.content[5].post_id
                    //val member_id_6 = responseData.result.content[5].member_id
                    //val vibe_id_6 = responseData.result.content[5].vibe_id
                    val vibe_image_6 = responseData.result.content[5].vibe_image

                    val imageView_6 = requireView().findViewById<ImageView>(R.id.imageButton_6)
                    imageView_6.clipToOutline = true
                    Glide.with(this@VibeMainFragment)
                        .load(vibe_image_6) // 불러올 이미지 url
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                    //    .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(imageView_6) // 이미지를 넣을 뷰

                    // post id 설정
                    viewBinding.imageButton1.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_1)
                        startActivity(intent)
                    }
                    viewBinding.imageButton2.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_2)
                        startActivity(intent)
                    }
                    viewBinding.imageButton3.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_3)
                        startActivity(intent)
                    }
                    viewBinding.imageButton4.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_4)
                        startActivity(intent)
                    }
                    viewBinding.imageButton5.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_5)
                        startActivity(intent)
                    }
                    viewBinding.imageButton6.setOnClickListener{
                        val intent = Intent(context, VibePostActivity::class.java)
                        intent.putExtra("post_id", post_id_6)
                        startActivity(intent)
                    }
                }
            }
        }


    }


    /**
     * 전체 게시물 조회
     */
    private fun getAllPosts() {
        val vibePostService = getRetrofit().create(VibePostApiInterface::class.java)
        vibePostService.postAllCheck(userToken, getPage())
            .enqueue(object : Callback<PostTagResponse> {
                override fun onResponse(
                    call: Call<PostTagResponse>,
                    response: Response<PostTagResponse>
                ) {
                    Log.d("[VIBE] GET_ALL_POSTS/SUCCESS", response.toString())
                    val resp: PostTagResponse = response.body()!!

                    // 서버 response 중 code 값에 따른 결과
                    when (resp.code) {
                        1000, 200 -> {
                            totalCount = resp.result.totalElements
                            isNext = !(resp.result.last)
                            mListAdapter.setPosts(resp.result.content as ArrayList<PostContentData>)
                            viewBinding.vibeMainAllPostsRv.scheduleLayoutAnimation()
                        }
                        else -> Log.d(
                            "[VIBE] GET_ALL_POSTS/FAILURE",
                            "${resp.code} / ${resp.message}"
                        )
                    }
                }

                override fun onFailure(call: Call<PostTagResponse>, t: Throwable) {
                    Log.d("[VIBE] GET_ALL_POSTS/FAILURE", t.message.toString())
                }
            })
        Log.d("[VIBE] GET_ALL_POSTS", "HELLO")
    }

    // 전체 게시물 조회 (2번) - Scroll이 끝에 닿으면 loadMore 호출
    private fun initScrollListener() {
        viewBinding.vibeMainNsv.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // Scroll이 최하단이면
            if (!v.canScrollVertically(1)) {
                val layoutManager = viewBinding.vibeMainAllPostsRv.layoutManager
                if (hasNextPage()) {
                    val lastVisibleItem = (layoutManager as StaggeredGridLayoutManager)
                        .findLastCompletelyVisibleItemPositions(null)[0]

                    // 마지막으로 보여진 아이템 position 이
                    // 전체 아이템 개수보다 8개 모자란 경우, 데이터를 loadMore 한다
                    if (layoutManager.itemCount <= lastVisibleItem + limit) {
                        loadMorePosts()
                        setHasNextPage(false)
                    }
                }
            }
        }
    }

    // 전체 게시물 조회 (3번) - RecyclerView에 더 보여줄 데이터를 로드하는 경우
    private fun loadMorePosts() {
        // 데이터에 null 추가 및 Adapter에 알림
        mListAdapter.setLoadingView(true)

        // 너무 빨리 데이터가 로드되면 스크롤 되는 Ui 를 확인하기 어려우므로,
        // Handler 를 사용하여 1초간 postDelayed 시킴
        val handler = android.os.Handler()
        handler.post {
            viewBinding.vibeMainNsv.post { viewBinding.vibeMainNsv.fullScroll(View.FOCUS_DOWN) }
        }
        handler.postDelayed({
            val vibePostService = getRetrofit().create(VibePostApiInterface::class.java)
            vibePostService.postAllCheck(userToken, getPage())
                .enqueue(object : Callback<PostTagResponse> {
                    override fun onResponse(
                        call: Call<PostTagResponse>,
                        response: Response<PostTagResponse>
                    ) {
                        val resp: PostTagResponse = response.body()!!

                        // 서버 response 중 code 값에 따른 결과
                        when (resp.code) {
                            1000 -> {
                                totalCount = resp.result.totalElements
                                isNext = !(resp.result.last)
                                mListAdapter.run {
                                    // 데이터에 null 제거 및 Adapter에 알림
                                    setLoadingView(false)
                                    // 새 데이터 추가
                                    addPosts(resp.result.content as ArrayList<PostContentData>)
                                }
                            }
                            else -> Log.d(
                                "[VIBE] GET_ALL_POSTS/FAILURE",
                                "${resp.code} / ${resp.message}"
                            )
                        }
                    }

                    override fun onFailure(call: Call<PostTagResponse>, t: Throwable) {
                        Log.d("[VIBE] GET_ALL_POSTS/FAILURE", t.message.toString())
                    }
                })
        }, 1000)
    }

    private fun getPage(): Int {
        page++
        return page
    }

    private fun hasNextPage(): Boolean {
        return isNext
    }

    private fun setHasNextPage(b: Boolean) {
        isNext = b

    }


    // 상태바 높이 구하기
    private fun Context.statusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
        else 0
    }
}