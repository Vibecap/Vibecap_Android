package com.example.vibecapandroid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vibecapandroid.coms.PostAllData
import com.example.vibecapandroid.databinding.ItemVibeMainAllPostBinding

class VibeMainAllPostRVAdapter(private val bodyDetailPartList: ArrayList<PostAllData>) :
    RecyclerView.Adapter<VibeMainAllPostRVAdapter.ViewHolder>() {
    interface MyItemClickListener {
        fun onItemClick(bodyDetailPartList: String)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemVibeMainAllPostBinding =
            ItemVibeMainAllPostBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(bodyDetailPartList[position])
//        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(bodyDetailPartList[position]) }
    }

    override fun getItemCount(): Int = bodyDetailPartList.size

    inner class ViewHolder(val binding: ItemVibeMainAllPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bodyDetailPartList: String) {
//            binding.itemBodyPartNameTv.text = bodyDetailPartList
        }
    }
}