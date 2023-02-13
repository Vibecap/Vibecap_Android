package com.example.vibecapandroid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vibecapandroid.coms.Notice
import com.example.vibecapandroid.databinding.ItemMypageAlarmBinding
import com.example.vibecapandroid.databinding.ItemMypageAlarmDateBinding

class MypageAlarmRVAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM_FIRST = 0   // 날짜 + 알림 (같은 날짜 내 첫 번째 아이템)
        private const val TYPE_ITEM_MIDDLE = 1  // 알림
        private const val TYPE_ITEM_LAST = 2    // 알림 (같은 날짜 내 마지막 아이템)
    }

    private var alarmList = ArrayList<Notice?>()

    var dateLastIdx = 0
    var date = ""
    var dateLastIdxPrev = 0
    var datePrev = ""
    var dateLastIdxNext = 0
    var dateNext = ""


    interface MyItemClickListener {
        fun onItemClick(alarmList: Notice)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun getItemViewType(position: Int): Int {
        // 날짜 비교 위해 (YYYY-MM-DD) 형태로 변환
        // 현 position의 아이템 날짜 변환
        dateLastIdx = alarmList[position]!!.time.lastIndexOf("T")
        date = alarmList[position]!!.time.removeRange(dateLastIdx, alarmList[position]!!.time.length)

        // 이전 position의 아이템 날짜 변환
        if (position != 0) {
            dateLastIdxPrev = alarmList[position - 1]!!.time.lastIndexOf("T")
            datePrev = alarmList[position - 1]!!.time.removeRange(
                dateLastIdxPrev,
                alarmList[position - 1]!!.time.length
            )
        }

        // 다음 position의 아이템 날짜 변환
        if (position != alarmList.size - 1) {
            dateLastIdxNext = alarmList[position + 1]!!.time.lastIndexOf("T")
            dateNext = alarmList[position + 1]!!.time.removeRange(
                dateLastIdxNext,
                alarmList[position + 1]!!.time.length
            )
        }


        // 가장 첫 번째 알림 or 이전 알림과 날짜가 다른 알림
        return if ((position == 0) || (date != datePrev)) {
            TYPE_ITEM_FIRST
        }
        // 가장 마지막 알림 or 다음 알림과 날짜가 다른 알림
        else if (position == alarmList.size - 1 || (date != dateNext)) {
            TYPE_ITEM_LAST
        }
        // 같은 날짜 내에서 중간에 속하는 알림
        else {
            TYPE_ITEM_MIDDLE
        }
    }

    fun setPosts(alarms: ArrayList<Notice>) {
        this.alarmList.apply {
            clear()
            addAll(alarms)
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM_FIRST -> {
                val binding = ItemMypageAlarmDateBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                FirstItemViewHolder(binding)
            }
            TYPE_ITEM_MIDDLE -> {
                val binding = ItemMypageAlarmBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                MiddleItemViewHolder(binding)
            }
            else -> {
                val binding = ItemMypageAlarmBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                LastItemViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_ITEM_FIRST -> {
                val firstViewHolder = holder as MypageAlarmRVAdapter.FirstItemViewHolder
                firstViewHolder.bind(alarmList[position]!!)

                if (position == alarmList.size - 1) {
                    firstViewHolder.binding.itemMypageBottomLineIv.visibility = View.GONE
                } else if (position != 0 && (date != datePrev) && (date != dateNext)) {
                    firstViewHolder.binding.itemMypageBottomLineIv.visibility = View.GONE
                } else if (position == 0 && (date != dateNext)) {
                    firstViewHolder.binding.itemMypageBottomLineIv.visibility = View.GONE
                } else {
                    firstViewHolder.binding.itemMypageBottomLineIv.visibility = View.VISIBLE
                }
                firstViewHolder.binding.itemMypageAlarmDateContentLayout.setOnClickListener { mItemClickListener.onItemClick(alarmList[position]!!) }
            }
            TYPE_ITEM_MIDDLE -> {
//                val animation: Animation = AnimationUtils.loadAnimation(
//                    context,
//                    R.anim.item_anim
//                )
//                holder.itemView.startAnimation(animation)
                val middleViewHolder = holder as MypageAlarmRVAdapter.MiddleItemViewHolder
                middleViewHolder.bind(alarmList[position]!!)
                middleViewHolder.binding.itemMypageAlarmLayout.setOnClickListener { mItemClickListener.onItemClick(alarmList[position]!!) }
            }
            TYPE_ITEM_LAST -> {
                val lastViewHolder = holder as MypageAlarmRVAdapter.LastItemViewHolder
                lastViewHolder.bind(alarmList[position]!!)
                lastViewHolder.binding.itemMypageAlarmLayout.setOnClickListener { mItemClickListener.onItemClick(alarmList[position]!!) }
            }
        }
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    // ItemView에 날짜 + 알림이 들어가는 경우
    inner class FirstItemViewHolder(val binding: ItemMypageAlarmDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alarmList: Notice) {
            // 날짜 설정
            var date = alarmList.time.replace("-", ". ").replace("T", ".")
            val dateLastIdx = date.lastIndexOf(".")
            date = date.removeRange(dateLastIdx + 1, date.length).replace(" ", "")
            binding.itemMypageAlarmDateTv.text = date

            // 내용 설정
            binding.itemMypageAlarmNicknameTv.text = alarmList.sender

            // 알림 종류에 따른 설정
            when (alarmList.event) {
                "COMMENT" -> {
                    binding.itemMypageAlarmContentTv.text = "내가 작성한 게시물에 댓글이 달렸습니다."
                    binding.itemMypageAlarmTypeTv.text = " 님의 댓글"
                    " : ${alarmList.summary}".also { binding.itemMypageAlarmSummaryTv.text = it }
                    binding.itemMypageAlarmIv.setImageResource(R.drawable.ic_activity_mypage_alarm_comment)
                }
                "LIKE" -> {
                    binding.itemMypageAlarmContentTv.text = "내가 작성한 게시물을 좋아요 했습니다."
                    binding.itemMypageAlarmTypeTv.text = " 님의 좋아요"
                    binding.itemMypageAlarmIv.setImageResource(R.drawable.ic_activity_mypage_alarm_like)
                }
                else -> {
                    binding.itemMypageAlarmContentTv.text = "내가 작성한 게시물에 대댓글이 달렸습니다."
                    " : ${alarmList.summary}".also { binding.itemMypageAlarmSummaryTv.text = it }
                    binding.itemMypageAlarmTypeTv.text = " 님의 대댓글"
                    binding.itemMypageAlarmIv.setImageResource(R.drawable.ic_activity_mypage_alarm_comment)
                }
            }
        }
    }

    // ItemView에 (중간) 알림이 들어가는 경우
    inner class MiddleItemViewHolder(val binding: ItemMypageAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alarmList: Notice) {
            // 내용 설정
            binding.itemMypageAlarmNicknameTv.text = alarmList.sender

            // 알림 종류에 따른 설정
            when (alarmList.event) {
                "COMMENT" -> {
                    binding.itemMypageAlarmContentTv.text = "내가 작성한 게시물에 댓글이 달렸습니다."
                    binding.itemMypageAlarmTypeTv.text = " 님의 댓글"
                    " : ${alarmList.summary}".also { binding.itemMypageAlarmSummaryTv.text = it }
                    binding.itemMypageAlarmIv.setImageResource(R.drawable.ic_activity_mypage_alarm_comment)
                }
                "LIKE" -> {
                    binding.itemMypageAlarmContentTv.text = "내가 작성한 게시물을 좋아요 했습니다."
                    binding.itemMypageAlarmTypeTv.text = " 님의 좋아요"
                    binding.itemMypageAlarmIv.setImageResource(R.drawable.ic_activity_mypage_alarm_like)
                }
                else -> {
                    binding.itemMypageAlarmContentTv.text = "내가 작성한 게시물에 대댓글이 달렸습니다."
                    binding.itemMypageAlarmTypeTv.text = " 님의 대댓글"
                    " : ${alarmList.summary}".also { binding.itemMypageAlarmSummaryTv.text = it }
                    binding.itemMypageAlarmIv.setImageResource(R.drawable.ic_activity_mypage_alarm_comment)
                }
            }

            // 점선 유무 설정 (동일 날짜 내 중간 아이템이므로 위아래 점선 모두 보이게 설정)
            binding.itemMypageTopLineIv.visibility = View.VISIBLE
            binding.itemMypageBottomLineIv.visibility = View.VISIBLE
        }
    }

    // ItemView에 (마지막) 알림이 들어가는 경우
    inner class LastItemViewHolder(val binding: ItemMypageAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alarmList: Notice) {
            // 내용 설정
            binding.itemMypageAlarmNicknameTv.text = alarmList.sender

            // 알림 종류에 따른 설정
            when (alarmList.event) {
                "COMMENT" -> {
                    binding.itemMypageAlarmContentTv.text = "내가 작성한 게시물에 댓글이 달렸습니다."
                    binding.itemMypageAlarmTypeTv.text = " 님의 댓글"
                    " : ${alarmList.summary}".also { binding.itemMypageAlarmSummaryTv.text = it }
                    binding.itemMypageAlarmIv.setImageResource(R.drawable.ic_activity_mypage_alarm_comment)
                }
                "LIKE" -> {
                    binding.itemMypageAlarmContentTv.text = "내가 작성한 게시물을 좋아요 했습니다."
                    binding.itemMypageAlarmTypeTv.text = " 님의 좋아요"
                    binding.itemMypageAlarmIv.setImageResource(R.drawable.ic_activity_mypage_alarm_like)
                }
                else -> {
                    binding.itemMypageAlarmContentTv.text = "내가 작성한 게시물에 대댓글이 달렸습니다."
                    binding.itemMypageAlarmTypeTv.text = " 님의 대댓글"
                    " : ${alarmList.summary}".also { binding.itemMypageAlarmSummaryTv.text = it }
                    binding.itemMypageAlarmIv.setImageResource(R.drawable.ic_activity_mypage_alarm_comment)
                }
            }

            // 점선 유무 설정 (동일 날짜 내 마지막 아이템이므로 위 점선만 보이게 설정)
            binding.itemMypageTopLineIv.visibility = View.VISIBLE
            binding.itemMypageBottomLineIv.visibility = View.GONE
        }
    }
}