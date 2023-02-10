package com.example.vibecapandroid

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.vibecapandroid.coms.PostContentData
import com.example.vibecapandroid.databinding.ItemVibeMainAllPostsBinding
import com.example.vibecapandroid.databinding.ItemVibeMainAllPostsLoadingBinding


class VibeMainAllPostsRVAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_LOADING = 1
    }

    private val posts = ArrayList<PostContentData?>()

    override fun getItemViewType(position: Int): Int {
        return when (posts[position]) {
            null -> TYPE_LOADING
            else -> TYPE_ITEM
        }
    }

    fun setPosts(posts: ArrayList<PostContentData>) {
        this.posts.apply {
            clear()
            addAll(posts)
        }
        notifyDataSetChanged()
    }

    fun addPosts(posts: ArrayList<PostContentData>) {
        this.posts.addAll(posts)
//        notifyDataSetChanged()
    }

    fun setLoadingView(b: Boolean) {
        if (b) {
            android.os.Handler().post {
                this.posts.add(null)
                notifyItemInserted(posts.size - 1)
            }
        } else {
            if (this.posts[posts.size - 1] == null) {
                this.posts.removeAt(posts.size - 1)
                notifyItemRemoved(posts.size)
            }
        }
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
        return if (viewType == TYPE_ITEM) {
            val binding = ItemVibeMainAllPostsBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
            ItemViewHolder(binding)
        } else {
            val binding = ItemVibeMainAllPostsLoadingBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
            LoadingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_ITEM -> {
                val animation: Animation = AnimationUtils.loadAnimation(
                    context,
                    R.anim.item_anim
                )
                holder.itemView.startAnimation(animation)
                val postViewHolder = holder as ItemViewHolder
                postViewHolder.bind(posts[position]!!)
            }
            TYPE_LOADING -> {
                val layoutParams =
                    holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                layoutParams.isFullSpan = true
            }
        }

        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(this.posts[position]!!) }

    }


    override fun getItemCount(): Int {
        return posts.size
    }

    // ItemView에 게시물이 들어가는 경우
    inner class ItemViewHolder(val binding: ItemVibeMainAllPostsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postContentData: PostContentData) {
            Glide.with(binding.root).load(postContentData.vibe_image)
                .placeholder(R.drawable.ic_activity_vibe_main_posttest)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.itemVibeMainAllPostsIv)
        }
    }

    // ItemView에 프로그레스바가 들어가는 경우
    inner class LoadingViewHolder(val binding: ItemVibeMainAllPostsLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

}