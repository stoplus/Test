package com.test.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.R
import com.test.databinding.ItemRateBinding
import com.test.network.models.domain.ReviewResult
import com.test.utils.toFullDate

class RateAdapter(
    private val list: MutableList<ReviewResult>
) : RecyclerView.Adapter<RateAdapter.RateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        return RateViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rate, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.updateItem(list[position])
    }

    inner class RateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRateBinding.bind(itemView)
        fun updateItem(response: ReviewResult) {
            setStarsIcon(response.rate, binding)
            val title = "${response.createdBy.username} ${itemView.resources.getString(R.string.comment_prefix)} ${response.createdDate.toFullDate()}"
            binding.rateDate.text = title
            if (response.comment.isNotEmpty()) {
                binding.rateComment.text = response.comment.trim()
            } else {
                binding.rateComment.visibility = View.GONE
            }
        }

        private fun setStarsIcon(rate: Int, binding: ItemRateBinding) {
            //check product rate
            when (rate) {
                1 -> setStarsIcon(binding, s2 = false, s3 = false, s4 = false, s5 = false)
                2 -> setStarsIcon(binding, s2 = true, s3 = false, s4 = false, s5 = false)
                3 -> setStarsIcon(binding, s2 = true, s3 = true, s4 = false, s5 = false)
                4 -> setStarsIcon(binding, s2 = true, s3 = true, s4 = true, s5 = false)
                5 -> setStarsIcon(binding, s2 = true, s3 = true, s4 = true, s5 = true)
            }
        }

        private fun setStarsIcon(binding: ItemRateBinding, s2: Boolean, s3: Boolean, s4: Boolean, s5: Boolean) {
            binding.rateContainer.star2.visibility = if (s2) View.VISIBLE else View.GONE
            binding.rateContainer.star3.visibility = if (s3) View.VISIBLE else View.GONE
            binding.rateContainer.star4.visibility = if (s4) View.VISIBLE else View.GONE
            binding.rateContainer.star5.visibility = if (s5) View.VISIBLE else View.GONE
        }
    }
}