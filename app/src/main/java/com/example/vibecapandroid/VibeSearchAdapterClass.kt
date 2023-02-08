package com.example.vibecapandroid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.vibecapandroid.coms.PostContentData
import com.example.vibecapandroid.databinding.ActivitySearchGridBinding
import com.example.vibecapandroid.databinding.ItemVibeMainAllPostsLoadingBinding

class VibeSearchAdapterClass(private val postContentData: ArrayList<PostContentData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_LOADING = 1
    }

    private var search_unFileteredList = postContentData
    private var search_filteredList = postContentData

    override fun getItemViewType(position: Int): Int {
        return when (search_filteredList.get(position)) {
            null -> TYPE_LOADING
            else -> TYPE_ITEM

        }
    }

    fun updateItem(list: ArrayList<PostContentData>){
        this.search_filteredList = list
    }

    interface MyItemClickListener{
        fun onItemClick(postContentData: PostContentData)
    }

    private lateinit var sItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        sItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM){
            val binding = ActivitySearchGridBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
            return sItemViewHolder(binding)
        } else {
            val binding = ItemVibeMainAllPostsLoadingBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
            return sLoadingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if( holder is sItemViewHolder){
            val item = search_filteredList.get(position)
            val itemHolder = holder as sItemViewHolder
            itemHolder.bind(item)
        } else if (holder is sLoadingViewHolder){

        }
        holder.itemView.setOnClickListener{ sItemClickListener.onItemClick(postContentData[position])}

    }

    inner class sLoadingViewHolder(binding : ItemVibeMainAllPostsLoadingBinding):
    RecyclerView.ViewHolder(binding.root){

    }
    override fun getItemCount(): Int {
        return search_filteredList.size
    }



    inner class sItemViewHolder(private val binding: ActivitySearchGridBinding):
            RecyclerView.ViewHolder(binding.root){
            fun bind(postContentData: PostContentData){
                Glide.with(binding.root)
                    .load(postContentData.vibe_image)
                    .placeholder(R.drawable.ic_activity_vibe_main_posttest)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.itemVibeSearchImgIv)
        }
    }

}