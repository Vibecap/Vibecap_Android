package com.example.vibecapandroid


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.*
import com.example.vibecapandroid.databinding.FragmentVibeMainBinding
import com.example.vibecapandroid.utils.getRetrofit
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Runnable

class VibeMainFragment : Fragment(), GetAllPostsView {
    private lateinit var viewBinding: FragmentVibeMainBinding
    private var viewPager: ViewPager2? = null
    private lateinit var getAllPostsView: GetAllPostsView

    private var dtoList: ArrayList<PostContentData> = ArrayList()
    private var items: ArrayList<PostContentData> = ArrayList()

    private lateinit var mMapLayoutManager: StaggeredGridLayoutManager
    private lateinit var mListAdapter: VibeMainAllPostRVAdapter
    private lateinit var mRecyclerView: RecyclerView

    val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private var isLoading = false

    var page: Int = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentVibeMainBinding.inflate(layoutInflater)

        setAllPostsView(this)

        viewBinding.apply {
//            lifecycleOwner = this@VibeMainFragment
//            activity = this@VibeMainFragment
        }


        getAllPosts(page)

        val view = inflater.inflate(R.layout.fragment_vibe_main, container, false)
        val tag1: TextView = view.findViewById(R.id.tv_tag1)
        val tag2: TextView = view.findViewById(R.id.tv_tag2)
        val tag3: TextView = view.findViewById(R.id.tv_tag3)
        val tag4: TextView = view.findViewById(R.id.tv_tag4)
        val tag5: TextView = view.findViewById(R.id.tv_tag5)
        val tag6: TextView = view.findViewById(R.id.tv_tag6)
        val tag7: TextView = view.findViewById(R.id.tv_tag7)
        val tag8: TextView = view.findViewById(R.id.tv_tag8)
        val tag9: TextView = view.findViewById(R.id.tv_tag9)


        //  post 테스트 용************
        val testPost: ImageView = view.findViewById(R.id.imageButton_1)
        testPost.setOnClickListener {
            val intent = Intent(context, VibePostActivity::class.java)
            intent.putExtra("post_id", 32)
            startActivity(intent)
        }
        //**********

        val search: ImageButton = view.findViewById(R.id.imageButton_search)
        search.setOnClickListener {
            val intent = Intent(context, VibeSearchActivity::class.java)
            startActivity(intent)
        }

        // 게시물 작성 플로팅 버튼
        viewBinding.vibeMainAddFab.setOnClickListener {
            val intent = Intent(context, HistoryPostActivity::class.java)
            startActivity(intent)
        }


        val mypage_alarm2: ImageButton = view.findViewById(R.id.imageButton_alarm)
        mypage_alarm2.setOnClickListener {
            val intent = Intent(context, MypageAlarmActivity::class.java)
            startActivity(intent)
        }

        val mypage_profile2: ImageButton = view.findViewById(R.id.imageButton_profile)
        mypage_profile2.setOnClickListener {
            val intent = Intent(context, MypageProfileActivity::class.java)
            startActivity(intent)
        }

        val addview: Button = view.findViewById(R.id.btn_addview)
        addview.setOnClickListener {
            val intent = Intent(context, VibeDetailActivity::class.java)
            startActivity(intent)
        }



        tag1.setOnClickListener {
            tag1.setTextColor(Color.BLACK)
            tag2.setTextColor(Color.GRAY)
            tag3.setTextColor(Color.GRAY)
            tag4.setTextColor(Color.GRAY)
            tag5.setTextColor(Color.GRAY)
            tag6.setTextColor(Color.GRAY)
            tag7.setTextColor(Color.GRAY)
            tag8.setTextColor(Color.GRAY)
            tag9.setTextColor(Color.GRAY)

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
            tag9.setTextColor(Color.GRAY)
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
            tag9.setTextColor(Color.GRAY)
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
            tag9.setTextColor(Color.GRAY)
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
            tag9.setTextColor(Color.GRAY)
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
            tag9.setTextColor(Color.GRAY)
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
            tag9.setTextColor(Color.GRAY)
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
            tag9.setTextColor(Color.GRAY)
            //api
            callTagAPI("분노한")
        }

        tag9.setOnClickListener {
            tag1.setTextColor(Color.GRAY)
            tag2.setTextColor(Color.GRAY)
            tag3.setTextColor(Color.GRAY)
            tag4.setTextColor(Color.GRAY)
            tag5.setTextColor(Color.GRAY)
            tag6.setTextColor(Color.GRAY)
            tag7.setTextColor(Color.GRAY)
            tag8.setTextColor(Color.GRAY)
            tag9.setTextColor(Color.BLACK)
            //api
            callTagAPI("심심한")
        }

        requestWeeklyAPI()



        return viewBinding.root
    }


    // api
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
                                    "result:${responseData?.result}\n"
                        )
                        if (responseData.is_success) {
                            // getSharedPreferences 해결해서 서버에서 데이터 가져와야함
                            Log.d(
                                "WeeklyResult2",
                                "WeeklyResult\n"
                            )
                            val imageView = view!!.findViewById<ImageView>(R.id.imageView_banner)
                            val defaultImage = R.drawable.ic_fragment_home_main_alarm
                            val url =
                                "https://firebasestorage.googleapis.com/v0/b/vibecap-ee692.appspot.com/o/b9bf7d74-88f3-4b06-952b-dc9c59f8090ajpg?alt=media"
                            Glide.with(this@VibeMainFragment)
                                .load(url) // 불러올 이미지 url
                                .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                                .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                                .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                                .into(imageView) // 이미지를 넣을 뷰

                            // 태그 띄우기

                            view!!.findViewById<TextView>(R.id.weeklyTagName).text = "#손흥민 #축구"
                        } else {
                            if (responseData.code == 3011) {
                                //Toast.makeText(this@VibeMainFragment,"해당 태그를 가진 게시물이 없습니다.", Toast.LENGTH_SHORT).show()

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
                            // getSharedPreferences 해결하기
                            //val editor = getSharedPreferences(
                            //"sharecropped",
                            //MODE_PRIVATE
                            //).edit()
                            //editor.putInt("post_id",responseData.result)
                            //editor.putInt("member_id",responseData.result[0].content[0].member_id)
                            //editor.putInt("vibe_id",responseData.result[0].content[0].vibe_id)
                            //editor.putString("vibe_image",responseData.result[1].vibe_image)
                            //editor.apply()
                            val imageView = view!!.findViewById<ImageView>(R.id.imageButton_1)
                            val defaultImage = R.drawable.image_ic_activity_history_album_list1
                            //val url = editor.putString("vibe_image",responseData.result[0].content[0].vibe_image)
                            val url =
                                "https://firebasestorage.googleapis.com/v0/b/vibecap-ee692.appspot.com/o/b9bf7d74-88f3-4b06-952b-dc9c59f8090ajpg?alt=media"
                            Glide.with(this@VibeMainFragment)
                                .load(url) // 불러올 이미지 url
                                .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                                .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                                .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                                .into(imageView) // 이미지를 넣을 뷰
                        } else {
                            if (responseData.code == 3011) {
                                // xml에 tvView 추가해서 문구 띄우기
                                //Toast.makeText(applicationContext,"해당 태그를 가진 게시물이 없습니다.", Toast.LENGTH_SHORT).show()

                            }
                        }
                    }
                }
            }

            // api 호출 실패시
            override fun onFailure(call: Call<PostTagResponse>, t: Throwable) {
                Log.e("retrofit onFailure2", "${t.message.toString()}")
            }
        })
    }

    // 전체 게시물 조회 API
    private fun getAllPosts(page: Int) {
        val vibePostService = getRetrofit().create(VibePostApiInterface::class.java)
        vibePostService.postAllCheck(userToken, page)
            .enqueue(object : Callback<PostTagResponse> {
                override fun onResponse(
                    call: Call<PostTagResponse>,
                    response: Response<PostTagResponse>
                ) {
                    Log.d("[VIBE] GET_ALL_POSTS/SUCCESS", response.toString())
                    val resp: PostTagResponse = response.body()!!

                    Log.d("[VIBE] GET_ALL_POSTS/CODE", resp.code.toString())

                    // 서버 response 중 code 값에 따른 결과
                    when (resp.code) {
                        1000 -> getAllPostsView.onGetAllPostsSuccess(resp.result)
                        else -> getAllPostsView.onGetAllPostsFailure(resp.code, resp.message)
                    }
                }

                override fun onFailure(call: Call<PostTagResponse>, t: Throwable) {
                    Log.d("[VIBE] GET_ALL_POSTS/FAILURE", t.message.toString())
                }
            })
        Log.d("[VIBE] GET_ALL_POSTS", "HELLO")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val pagerAdapter = VibeWeeklyAdapter(requireActivity())

        viewPager?.adapter = pagerAdapter

    }

    private fun setAllPostsView(
        getAllPostsView: GetAllPostsView
    ) {
        this.getAllPostsView = getAllPostsView
    }

    /**
     * 게시물 전체 조회 (태그 X) 성공, 실패 처리
     */
    override fun onGetAllPostsSuccess(result: PostAllData) {
        // 게시물 전체 조회 rv 설정
        val vibeMainAllPostRVAdapter =
            VibeMainAllPostRVAdapter(result.content as ArrayList<PostContentData>)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        viewBinding.vibeMainAllPostsRv.layoutManager = layoutManager

        viewBinding.vibeMainAllPostsRv.adapter = vibeMainAllPostRVAdapter

        vibeMainAllPostRVAdapter.setMyItemClickListener(object :
            VibeMainAllPostRVAdapter.MyItemClickListener {
            override fun onItemClick(postContentData: PostContentData) {
                val intent = Intent(context, VibePostActivity::class.java)
                intent.putExtra("post_id", postContentData.post_id)
                startActivity(intent)
            }

        })
//        setData(result.content)
//        initAdapter()
//        initScrollListener()
    }

    override fun onGetAllPostsFailure(code: Int, message: String) {
        Log.d("[VIBE] GET_ALL_POSTS/FAILURE", "$code / $message")
    }


    private fun setData(result: ArrayList<PostContentData>) {
//        dtoList= intent.getSerializableExtra(EXTRA_TITLE) as ArrayList<PostContentData>
        dtoList = result as ArrayList<PostContentData>
        for (i in 0 until 8) {
            items.add(result[i])
        }
    }

    //1. RecyclerView 생성
    private fun initAdapter() {
        mListAdapter = VibeMainAllPostRVAdapter(items)
        mMapLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        viewBinding.vibeMainAllPostsRv.adapter = mListAdapter

    }

    // 2. scroll이 끝에 닿으면 데이터에 null 추가 및 Adapter에 알림
//    private fun initScrollListener() {
//        viewBinding.vibeMainNsv.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            if (!isLoading) {
//                if (!v.canScrollVertically(1)) {
//                    moreItems()
//                    isLoading = true
//                }
//
////                if ((viewBinding.vibeMainAllPostsRv.layoutManager as StaggeredGridLayoutManager)!!.findLastCompletelyVisibleItemPositions(
////                        null
////                    )[0] == items.size - 1
////                ) {
////                    Log.e("true", "True")
////                    moreItems()
////                    isLoading = true
////                }
//
//            }
//        }
//    }

    //3. null 제거 후 새로운 데이터 추가 및 Adapter에 알림
//    fun moreItems() {
//        mRecyclerView = viewBinding.vibeMainAllPostsRv
//
//        val runnable = Runnable {
////            items.add()
//            mListAdapter.notifyItemInserted(items.size - 1)
//        }
//        mRecyclerView.post(runnable)
//
//        CoroutineScope(mainDispatcher).launch {
//            delay(2000)
//            val runnable2 = Runnable {
//                items.removeAt(items.size - 1)
//                val scrollPosition = items.size
//                mListAdapter.notifyItemRemoved(scrollPosition)
//
//                page++
//                getAllPosts(page)
//                var currentSize = scrollPosition
//                var nextLimit = currentSize + 8
//                Log.e("hello", "${nextLimit}")
//                if (currentSize < dtoList.size - 8) {
//                    while (currentSize - 1 < nextLimit) {
//                        items.add(dtoList[currentSize])
//                        currentSize++
//                    }
//                } else {
//                    while (currentSize != dtoList.size) {
//                        items.add(dtoList[currentSize])
//                        currentSize++
//                    }
//                }
//                mListAdapter.updateItem(items)
//                isLoading = false
//            }
//            runnable2.run()
//        }
//    }

}

interface GetAllPostsView {
    fun onGetAllPostsSuccess(result: PostAllData)
    fun onGetAllPostsFailure(code: Int, message: String)
}