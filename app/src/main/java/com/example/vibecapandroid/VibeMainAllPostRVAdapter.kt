package com.example.vibecapandroid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.vibecapandroid.coms.PostContentData
import com.example.vibecapandroid.databinding.ItemVibeMainAllPostsBinding
import com.example.vibecapandroid.databinding.ItemVibeMainAllPostsLoadingBinding


class VibeMainAllPostRVAdapter(private val postContentData: ArrayList<PostContentData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_LOADING = 1
    }

    private var unFilteredList = postContentData
    private var filteredList = postContentData

    override fun getItemViewType(position: Int): Int {
        return when (filteredList.get(position)) {
            null -> TYPE_LOADING
            else -> TYPE_ITEM

        }
    }

    fun updateItem(list: ArrayList<PostContentData>) {
        this.filteredList = list
    }

    interface MyItemClickListener {
        fun onItemClick(postContentData: PostContentData)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
//        val binding: ItemVibeMainAllPostsBinding =
//            ItemVibeMainAllPostsBinding.inflate(
//                LayoutInflater.from(viewGroup.context),
//                viewGroup,
//                false
//            )

        if (viewType == TYPE_ITEM) {
            val binding = ItemVibeMainAllPostsBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
            return ItemViewHolder(binding)
        } else {
            val binding = ItemVibeMainAllPostsLoadingBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
            return LoadingViewHolder(binding)
        }

//        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        holder.bind(postContentData[position])

        if (holder is ItemViewHolder) {
            val item = filteredList.get(position)
            val itemHolder = holder as ItemViewHolder
            itemHolder.bind(item)
        } else if (holder is LoadingViewHolder) {

        }
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(postContentData[position]) }
    }

    // 아이템뷰에 프로그레스바가 들어가는 경우
    inner class LoadingViewHolder(binding: ItemVibeMainAllPostsLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    inner class ItemViewHolder(private val binding: ItemVibeMainAllPostsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postContentData: PostContentData) {
            Glide.with(binding.root).load(postContentData.vibe_image)
                .placeholder(R.drawable.ic_activity_vibe_main_posttest)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.itemVibeMainAllPostsIv)

        }
    }

}