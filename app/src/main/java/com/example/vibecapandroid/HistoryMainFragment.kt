package com.example.vibecapandroid

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64.NO_WRAP
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vibecapandroid.coms.HistoryAllResponse
import com.example.vibecapandroid.coms.HistoryApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Base64.*


class HistoryMainFragment : Fragment() {
    //private lateinit var viewBinding: FragmentHistoryMainBinding
    private var recyclerView: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    public var arrayList:ArrayList<HistoryMainImageClass> ? = null
    private var historyMainAdapters:HistoryMainAdaptersClass ? = null

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService=retrofit.create(HistoryApiInterface::class.java)

    private var Token:String= userToken
    private val memberId:Long=6

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arrayList = ArrayList()
        setDataInList()
        historyMainAdapters = HistoryMainAdaptersClass(requireContext(),arrayList!!)
        return inflater.inflate(R.layout.fragment_history_main, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view?.findViewById(R.id.history_main_recyclerview)
        gridLayoutManager = GridLayoutManager(requireContext(),3,
            LinearLayoutManager.VERTICAL,false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = historyMainAdapters
    }


}
