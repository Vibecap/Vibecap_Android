package com.example.vibecapandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.vibecapandroid.coms.PostContentData
import com.example.vibecapandroid.databinding.FragmentVibeMainAllPostsBinding
import com.example.vibecapandroid.databinding.FragmentVibeMainBinding
import com.example.vibecapandroid.databinding.ItemVibeMainAllPostsBinding
import com.example.vibecapandroid.databinding.ItemVibeMainAllPostsLoadingBinding


class VibeMainAllPostsTestRVAdapter(private val postContentData: ArrayList<PostContentData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
    private val TYPE_FOOTER = 2


    var count = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        count += 1

        return if (viewType == ITEM) {
            ItemViewHolder(FragmentVibeMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            Item2ViewHolder(FragmentVibeMainAllPostsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        if (getItemViewType(position) == ITEM)
            (holder as ItemViewHolder).bind()
        else if (getItemViewType(position) == ITEM2) (holder as Item2ViewHolder).bind()
    }

    override fun getItemCount(): Int {
        return 50
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return TYPE_HEADER
        else
            return TYPE_ITEM;
    }

    inner class ItemViewHolder(var binding: FragmentVibeMainBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
//            binding.tv.text = "${adapterPosition}번째 홀더입니다"
        }
    }

    inner class Item2ViewHolder(var binding: FragmentVibeMainAllPostsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
//            binding.tv.text = "${adapterPosition}번째 홀더입니다"
        }
    }

    companion object {
        private const val ITEM = 1
        private const val ITEM2 = 2
    }

}