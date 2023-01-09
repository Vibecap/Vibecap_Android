package com.example.vibecapandroid


import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vibecapandroid.databinding.FragmentHistoryMainBinding

class HistoryMainFragment : Fragment() {
    //private lateinit var viewBinding: FragmentHistoryMainBinding

    private var recyclerView: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var arrayList:ArrayList<HistoryMainImageClass> ? = null
    private var historyMainAdapters:HistoryMainAdaptersClass ? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        arrayList = ArrayList()
        arrayList = setDataInList()
        historyMainAdapters = HistoryMainAdaptersClass(requireContext(),arrayList!!)

        return inflater.inflate(R.layout.fragment_history_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //viewBinding= FragmentHistoryMainBinding.inflate(layoutInflater)
        //viewBinding은 어떻게 적용해야할지 잘 모르겠음
        //여기부터 실행 코드
        recyclerView = view?.findViewById(R.id.history_main_recyclerview)
        gridLayoutManager = GridLayoutManager(requireContext(),3,
            LinearLayoutManager.VERTICAL,false)

        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = historyMainAdapters
    }

    private fun setDataInList(): ArrayList<HistoryMainImageClass>{
        var items: ArrayList<HistoryMainImageClass> = ArrayList()

        //이곳에 다음과 같이 계속 추가하면 됨

        items.add(HistoryMainImageClass(R.drawable.image_ic_activity_history_album_list1))
        items.add(HistoryMainImageClass(R.drawable.image_ic_activity_history_album_list2))
        items.add(HistoryMainImageClass(R.drawable.image_ic_activity_history_album_list3))

        return items
    }

}
