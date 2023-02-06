package com.example.vibecapandroid

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.*
import com.example.vibecapandroid.databinding.ItemVibeCommentBinding
import com.example.vibecapandroid.utils.getRetrofit
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VibeCommentRVAdapter(
    val context: Context,
    private val commentsResult: ArrayList<CommentsResult>
) :
    RecyclerView.Adapter<VibeCommentRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(commentsResult: CommentsResult)
        fun onSubCommentAddClick(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemVibeCommentBinding =
            ItemVibeCommentBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(commentsResult[position])
        holder.binding.itemVibeCommentSubCommentAddTv.setOnClickListener {
            holder.binding.itemVibeCommentLayout.setBackgroundResource(R.color.purple_100)
            mItemClickListener.onSubCommentAddClick((position)) }

//        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(commentsResult[position]) }
    }

    override fun getItemCount(): Int = commentsResult.size

    inner class ViewHolder(val binding: ItemVibeCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(commentsResult: CommentsResult) {
            Glide.with(binding.root).load(commentsResult.profileImg)
                .placeholder(R.drawable.ic_activity_vibe_post_profile).circleCrop()
                .into(binding.itemVibeCommentProfileImgIv)
            binding.itemVibeCommentNicknameTv.text = commentsResult.nickname
            binding.itemVibeCommentBodyTv.text = commentsResult.commentBody
            // createdDate 설정
            var createdDate = commentsResult.createdDate.replace("-", ". ").replace("T", ". ")
            val dateLastIdx = createdDate.lastIndexOf(":")
            createdDate = createdDate.removeRange(dateLastIdx, createdDate.length)
            binding.itemVibeCommentDateTv.text = createdDate

            // 대댓글
            if (commentsResult.subComment.isEmpty()) {
                binding.itemVibeSubCommentRv.visibility = View.GONE
            } else {
                binding.itemVibeSubCommentRv.visibility = View.VISIBLE
                // 대댓글 rv 연결
                val vibeSubCommentRVAdapter =
                    VibeSubCommentRVAdapter(context, commentsResult.subComment)
                binding.itemVibeSubCommentRv.adapter = vibeSubCommentRVAdapter
            }

            binding.itemVibeCommentLayout.setBackgroundResource(R.color.white)



            // 댓글 메뉴
            binding.itemVibeCommentMenuBtn.setOnClickListener {
                val commentMenuBottomSheetView = LayoutInflater.from(context)
                    .inflate(R.layout.bottom_sheet_vibe_comment_menu, binding.root, false)

                val commentMenuBottomSheetDialog =
                    BottomSheetDialog(context, R.style.CustomBottomSheetDialog)
                commentMenuBottomSheetDialog.setContentView(commentMenuBottomSheetView)
                commentMenuBottomSheetDialog.show()
            }

        }

        fun removeItem() {
            binding.itemVibeCommentLayout.setBackgroundResource(R.color.purple_100)
//        albumList.removeAt(position)
//        notifyDataSetChanged()
        }

    }
}


//interface OnItemClickValue {
//    fun onValueChange(value: Boolean)
//}