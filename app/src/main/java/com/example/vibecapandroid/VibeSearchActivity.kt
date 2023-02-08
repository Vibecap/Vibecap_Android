package com.example.vibecapandroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.vibecapandroid.coms.PostContentData
import com.example.vibecapandroid.coms.PostTagResponse
import com.example.vibecapandroid.coms.VibePostApiInterface
import com.example.vibecapandroid.databinding.ActivityVibeSearchBinding
import com.example.vibecapandroid.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VibeSearchActivity : AppCompatActivity() {

    lateinit var binding: ActivityVibeSearchBinding

    private lateinit var vibeSearchLayoutManager: StaggeredGridLayoutManager
    private lateinit var vibeSearchRVAdapter: VibeMainAllPostsRVAdapter
    private lateinit var mRecyclerView: RecyclerView

    private var totalCount = 0 // 전체 아이템 개수
    private var isNext = false // 다음 페이지 유무
    private var page = -1      // 현재 페이지
    private var limit = 8     // 한 번에 가져올 아이템 수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVibeSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        overridePendingTransition(R.anim.slide_in, R.anim.fade_out)

        binding.vibeSearchBackBtn.setOnClickListener { finish() }

        // 게시물 RecyclerView adapter 연결
        vibeSearchRVAdapter = VibeMainAllPostsRVAdapter(this)
        vibeSearchLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.vibeSearchRv.adapter = vibeSearchRVAdapter

        vibeSearchRVAdapter.setMyItemClickListener(object :
            VibeMainAllPostsRVAdapter.MyItemClickListener {
            override fun onItemClick(postContentData: PostContentData) {
                val intent = Intent(this@VibeSearchActivity, VibePostActivity::class.java)
                intent.putExtra("post_id", postContentData.post_id)
                startActivity(intent)
            }
        })


        // 검색 (tag별)
        binding.vibeSearchBtn.setOnClickListener {
            binding.vibeSearchEt.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.vibeSearchEt.windowToken, 0)

            page = -1
            searchPosts(binding.vibeSearchEt.text.toString())
            initScrollListener()
        }

        binding.vibeSearchEt.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.vibeSearchEt.clearFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.vibeSearchEt.windowToken, 0)

                page = -1
                searchPosts(binding.vibeSearchEt.text.toString())
                initScrollListener()

                return@OnEditorActionListener true
            }
            false
        })

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out)
    }

    /**
     * 게시물 전체 조회 (태그별)
     */
    private fun searchPosts(tagName: String) {
        val vibePostService = getRetrofit().create(VibePostApiInterface::class.java)
        vibePostService.postAllCheckWithTag(userToken, tagName, getPage())
            .enqueue(object : Callback<PostTagResponse> {
                override fun onResponse(
                    call: Call<PostTagResponse>,
                    response: Response<PostTagResponse>
                ) {
                    Log.d("[VIBE] SEARCH_POSTS/SUCCESS", response.toString())
                    val resp: PostTagResponse = response.body()!!

                    // 서버 response 중 code 값에 따른 결과
                    when (resp.code) {
                        1000 -> {
                            binding.vibeSearchEmptyTv.visibility = View.GONE
                            binding.vibeSearchRv.visibility = View.VISIBLE

                            totalCount = resp.result.totalElements
                            isNext = !(resp.result.last)
                            vibeSearchRVAdapter.setPosts(resp.result.content as ArrayList<PostContentData>)
                            binding.vibeSearchRv.scheduleLayoutAnimation()
                        }
                        3011 -> {
                            binding.vibeSearchEmptyTv.visibility = View.VISIBLE
                            binding.vibeSearchRv.visibility = View.GONE
                        }
                        else -> Log.d(
                            "[VIBE] SEARCH_POSTS/FAILURE",
                            "${resp.code} / ${resp.message}"
                        )
                    }
                }

                override fun onFailure(call: Call<PostTagResponse>, t: Throwable) {
                    Log.d("[VIBE] SEARCH_POSTS/FAILURE", t.message.toString())
                }
            })
    }

    // RecyclerView에 더 보여줄 데이터를 로드하는 경우
    private fun loadMorePosts() {
        vibeSearchRVAdapter.setLoadingView(true)

        // 너무 빨리 데이터가 로드되면 스크롤 되는 Ui 를 확인하기 어려우므로,
        // Handler 를 사용하여 1초간 postDelayed 시킴
        val handler = android.os.Handler()
        handler.post {
            binding.vibeSearchRv.post {
                binding.vibeSearchRv.smoothScrollToPosition(
                    vibeSearchRVAdapter.itemCount - 1
                )
            }
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
                                vibeSearchRVAdapter.run {
                                    setLoadingView(false)
                                    addPosts(resp.result.content as ArrayList<PostContentData>)
                                }
                            }
                            else -> Log.d(
                                "[VIBE] SEARCH_POSTS/FAILURE",
                                "${resp.code} / ${resp.message}"
                            )
                        }
                    }

                    override fun onFailure(call: Call<PostTagResponse>, t: Throwable) {
                        Log.d("[VIBE] SEARCH_POSTS/FAILURE", t.message.toString())
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

    // scroll이 끝에 닿으면 데이터에 null 추가 및 Adapter에 알림
    private fun initScrollListener() {
        binding.vibeSearchRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1)) {

                    val layoutManager = binding.vibeSearchRv.layoutManager
                    if (hasNextPage()) {
                        val lastVisibleItem = (layoutManager as StaggeredGridLayoutManager)
                            .findLastCompletelyVisibleItemPositions(null)[0]

                        // 마지막으로 보여진 아이템 position 이
                        // 전체 아이템 개수보다 8개 모자란 경우, 데이터를 loadMore 한다
                        if (layoutManager.itemCount <= lastVisibleItem + 8) {
                            loadMorePosts()
                            setHasNextPage(false)
                        }
                    }
                }
            }
        })
    }
}