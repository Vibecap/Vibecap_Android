package com.example.vibecapandroid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.CommentsResult
import com.example.vibecapandroid.coms.SubCommentResult
import com.example.vibecapandroid.databinding.ItemVibeCommentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class VibeCommentRVAdapter(
    val context: Context,
    private val commentsResult: ArrayList<CommentsResult>
) :
    RecyclerView.Adapter<VibeCommentRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(commentsResult: CommentsResult)
        fun onCommentMenuClick(commentsResult: CommentsResult)
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
//        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(commentsResult[position]) }
    }

    override fun getItemCount(): Int = commentsResult.size

    inner class ViewHolder(private val binding: ItemVibeCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(commentsResult: CommentsResult) {
            Glide.with(binding.root).load(commentsResult.profileImg).circleCrop()
                .into(binding.itemVibeCommentProfileImgIv)
            binding.itemVibeCommentNicknameTv.text = commentsResult.nickname
            binding.itemVibeCommentBodyTv.text = commentsResult.commentBody
//            binding.itemVibeCommentDateTv.text = commentsResult.date // 생각해보니 서버에서 댓글 날짜 안 보냄...

            // 대댓글
            if (commentsResult.subComment.isEmpty()) {
                binding.itemVibeSubCommentRv.visibility = View.GONE
            } else {
                binding.itemVibeSubCommentRv.visibility = View.VISIBLE
                // 대댓글 rv 연결
                val vibeSubCommentRVAdapter =
                    VibeSubCommentRVAdapter(context, commentsResult.subComment)
                binding.itemVibeSubCommentRv.adapter = vibeSubCommentRVAdapter

//                vibeSubCommentRVAdapter.setMyItemClickListener(object :
//                    VibeSubCommentRVAdapter.MyItemClickListener {
//                    override fun onItemClick(subCommentsResult: SubCommentResult) {
//                    }
//
//                })

            }

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
    }
}