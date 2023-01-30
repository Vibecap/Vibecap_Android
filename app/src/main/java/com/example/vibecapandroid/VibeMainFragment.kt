package com.example.vibecapandroid


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.vibecapandroid.coms.PostAllResponse
import com.example.vibecapandroid.coms.PostDetailResponse
import com.example.vibecapandroid.coms.VibePostAllInterface
import com.example.vibecapandroid.coms.VibePostApiInterface
import com.example.vibecapandroid.databinding.FragmentVibeMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VibeMainFragment : Fragment() {
    private lateinit var viewBinding: FragmentVibeMainBinding
    private var viewPager: ViewPager2? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        viewBinding = FragmentVibeMainBinding.inflate(layoutInflater)
        val view = inflater.inflate(R.layout.fragment_vibe_main, container, false)

        //  post 테스트 용************
        val testPost :ImageView = view.findViewById(R.id.imageButton_1)
        testPost.setOnClickListener{
            val intent = Intent(context, VibePostActivity::class.java)
            startActivity(intent)
        }
        //**********

        val search :ImageButton = view.findViewById(R.id.imageButton_search)
        search.setOnClickListener {
            val intent = Intent(context, VibeSearchActivity::class.java)
            startActivity(intent)
        }

        val addpost : Button = view.findViewById(R.id.btn_addpost)
        addpost.setOnClickListener {
            val intent = Intent(context, HistoryPostActivity::class.java)
            startActivity(intent)
        }

        val mypage_alarm2: ImageButton = view.findViewById(R.id.imageButton_alarm)
        mypage_alarm2.setOnClickListener {
            val intent = Intent(context, MypageAlarmActivity::class.java)
            startActivity(intent)
        }

        val mypage_profile2:ImageButton = view.findViewById(R.id.imageButton_profile)
        mypage_profile2.setOnClickListener {
            val intent = Intent(context, MypageProfileActivity::class.java)
            startActivity(intent)
        }

        val addview :Button = view.findViewById(R.id.btn_addview)
        addview.setOnClickListener{
            val intent = Intent(context, VibeDetailActivity::class.java)
            startActivity(intent)
        }

        tagDetailView()
        //requestToRestAPI()
        return view

    }

    //태그별 이동
    @SuppressLint("ResourceAsColor")
    private fun tagDetailView() {
       viewBinding.tvTag1.setOnClickListener{
            viewBinding.tvTag1.setTextColor(ContextCompat.getColor(this.requireContext(),R.color.black))
       }
        viewBinding.tvTag2.setOnClickListener{
            viewBinding.tvTag2.setTextColor(R.color.black)
        }
        viewBinding.tvTag3.setOnClickListener {
            viewBinding.tvTag3.setTextColor(resources.getColor(R.color.black))
        }
        viewBinding.tvTag4.setOnClickListener {
            viewBinding.tvTag4.setTextColor(resources.getColor(R.color.black))
        }
        viewBinding.tvTag5.setOnClickListener {
            viewBinding.tvTag5.setTextColor(resources.getColor(R.color.black))
        }
        viewBinding.tvTag6.setOnClickListener {
            viewBinding.tvTag6.setTextColor(resources.getColor(R.color.black))
        }
        viewBinding.tvTag7.setOnClickListener {
            viewBinding.tvTag7.setTextColor(resources.getColor(R.color.black))
        }
        viewBinding.tvTag8.setOnClickListener {
            viewBinding.tvTag8.setTextColor(resources.getColor(R.color.black))
        }
        viewBinding.tvTag9.setOnClickListener {
            viewBinding.tvTag9.setTextColor(resources.getColor(R.color.black))
        }
    }

    // api
    object RetrofitObject {
        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getApiService(): VibePostAllInterface {
            return getRetrofit().create(VibePostAllInterface::class.java)
        }
    }
    private fun requestToRestAPI() {
        VibeMainFragment.RetrofitObject.getApiService().postAllCheck("아침").enqueue(object :
            Callback<PostAllResponse> {
            // api 호출 성공시
            override fun onResponse(call: Call<PostAllResponse>, response: Response<PostAllResponse>) {
                //Toast.makeText(applicationContext, "success", Toast.LENGTH_SHORT).show()
                setTag_1(response.code(), response.body())
                val responseData = response.body()
                Log.d(
                    "postCapture",
                    "postCapture\n"+
                            "isSuccess:${responseData?.is_success}\n " +
                            "Code: ${responseData?.code} \n" +
                            "Message:${responseData?.message} \n" )
            }

            // api 호출 실패시
            override fun onFailure(call: Call<PostAllResponse>, t: Throwable) {
                Log.e("retrofit onFailure", "${t.message.toString()}")
            }
        })
    }

    private fun setTag_1(code: Int, body: PostAllResponse?) {
        viewBinding.imageButton1
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val pagerAdapter = VibeWeeklyAdapter(requireActivity())

        viewPager?.adapter = pagerAdapter

    }


}