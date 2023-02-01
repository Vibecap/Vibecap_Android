package com.example.vibecapandroid

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.SubCommentResult
import com.example.vibecapandroid.databinding.ItemVibeSubCommentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class VibeSubCommentRVAdapter(val context: Context, private val subCommentsResult: ArrayList<SubCommentResult>) :
    RecyclerView.Adapter<VibeSubCommentRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(subCommentsResult: SubCommentResult)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemVibeSubCommentBinding =
            ItemVibeSubCommentBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subCommentsResult[position])
//        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(subCommentsResult[position]) }
    }

    override fun getItemCount(): Int = subCommentsResult.size

    inner class ViewHolder(private val binding: ItemVibeSubCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subCommentsResult: SubCommentResult) {
            Glide.with(binding.root).load(subCommentsResult.profileImg).circleCrop()
                .into(binding.itemVibeSubCommentProfileImgIv)
            binding.itemVibeSubCommentNicknameTv.text = subCommentsResult.nickname
            binding.itemVibeSubCommentBodyTv.text = subCommentsResult.subCommentBody
//            binding.itemVibeCommentDateTv.text = commentsResult.date // 생각해보니 서버에서 대댓글 날짜 안 보냄...

            // 대댓글 메뉴
            binding.itemVibeSubCommentMenuBtn.setOnClickListener {
                val commentMenuBottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_vibe_comment_menu, binding.root, false)

                val commentMenuBottomSheetDialog =
                    BottomSheetDialog(context, R.style.CustomBottomSheetDialog)
                commentMenuBottomSheetDialog.setContentView(commentMenuBottomSheetView!!)
                commentMenuBottomSheetDialog.show()
            }
        }
    }
}