package com.example.vibecapandroid

import android.content.Context
import android.content.Intent
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
import com.example.vibecapandroid.coms.HistoryApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Base64.*
import android.widget.ImageButton
import androidx.core.view.WindowInsetsControllerCompat
import com.example.vibecapandroid.databinding.FragmentHistoryMainBinding

public var historyMainAdapters:HistoryMainAdaptersClass ? = null

class HistoryMainFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null


    private var Token:String= userToken
    private val memberId:Long=6

    lateinit var binding: FragmentHistoryMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryMainBinding.inflate(layoutInflater)
        // 상태바 설정
        val windowController = WindowInsetsControllerCompat(requireActivity().window, requireActivity().window.decorView)
        windowController.isAppearanceLightStatusBars = true
        binding.historyMainLayout.setPadding(0,requireContext().statusBarHeight(), 0, 0)

        Log.d("arrayList","$arrayList")
        historyMainAdapters = HistoryMainAdaptersClass(requireContext(),arrayList!!)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view?.findViewById(R.id.history_main_recyclerview)
        gridLayoutManager = GridLayoutManager(requireContext(),3,
            LinearLayoutManager.VERTICAL,false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = historyMainAdapters
        historyMainAdapters?.notifyDataSetChanged()

        val galleryButton : ImageButton? = view?.findViewById(R.id.history_phone_gallery_button)
        galleryButton?.setOnClickListener{
            val nextIntent = Intent(context, HistoryPhoneGalleryActivity::class.java)
            startActivity(nextIntent)
        }



    }

    private fun Context.statusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
        else 0
    }

}
