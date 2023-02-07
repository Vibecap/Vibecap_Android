package com.example.vibecapandroid

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.vibecapandroid.coms.PostContentData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.vibecapandroid.databinding.ActivityVibeDetiailgridBinding
import com.example.vibecapandroid.databinding.ItemVibeMainAllPostsLoadingBinding

class VibeDetailAdapterClass(private val postContentData: ArrayList<PostContentData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_LOADING = 1
    }

    private var detail_unFileteredList = postContentData
    private var detail_filteredList = postContentData

    override fun getItemViewType(position: Int): Int {
        return when (detail_filteredList.get(position)) {
            null -> TYPE_LOADING
            else -> TYPE_ITEM

        }
    }

    fun updateItem(list: ArrayList<PostContentData>){
        this.detail_filteredList = list
    }

    interface MyItemClickListener {
        fun onItemClick(postContentData: PostContentData)
    }

    private lateinit var dItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        dItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM){
            val binding = ActivityVibeDetiailgridBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
            return dItemViewHolder(binding)
        } else {
            val binding = ItemVibeMainAllPostsLoadingBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
            return dLoadingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if( holder is dItemViewHolder){
            val item = detail_filteredList.get(position)
            val itemHolder = holder as dItemViewHolder
            itemHolder.bind(item)
        } else if (holder is dLoadingViewHolder){

        }
        holder.itemView.setOnClickListener{ dItemClickListener.onItemClick(postContentData[position])}
    }

    inner class dLoadingViewHolder(binding: ItemVibeMainAllPostsLoadingBinding) :
    RecyclerView.ViewHolder(binding.root){

    }
    override fun getItemCount(): Int {
        return detail_filteredList.size
    }

    inner class dItemViewHolder(private val binding:ActivityVibeDetiailgridBinding):
            RecyclerView.ViewHolder(binding.root){
                fun bind(postContentData: PostContentData){
                    Glide.with(binding.root)
                        .load(postContentData.vibe_image)
                        .placeholder(R.drawable.ic_activity_vibe_main_posttest)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.itemVibeDatailImgIv)
                }
            }


}