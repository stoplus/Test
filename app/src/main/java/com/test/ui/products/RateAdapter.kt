package com.test.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.R
import com.test.network.models.ReviewModel
import com.test.utils.toFullDate
import kotlinx.android.synthetic.main.item_rate.view.*
import kotlinx.android.synthetic.main.view_stars.view.*

class RateAdapter(
    private val list: MutableList<ReviewModel>
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
        fun updateItem(model: ReviewModel) {
            setStarsIcon(model.rate)
            val title = "${model.createdBy?.username} ${itemView.resources.getString(R.string.comment_prefix)} ${model.createdDate.toFullDate()}"
            itemView.rateDate.text = title
            if (model.comment.isNotEmpty()) {
                itemView.rateComment.text = model.comment.trim()
            } else {
                itemView.rateComment.visibility = View.GONE
            }
        }

        private fun setStarsIcon(rate: Int) {
            //check product rate
            when (rate) {
                1 -> setStarsIcon(s2 = false, s3 = false, s4 = false, s5 = false)
                2 -> setStarsIcon(s2 = true, s3 = false, s4 = false, s5 = false)
                3 -> setStarsIcon(s2 = true, s3 = true, s4 = false, s5 = false)
                4 -> setStarsIcon(s2 = true, s3 = true, s4 = true, s5 = false)
                5 -> setStarsIcon(s2 = true, s3 = true, s4 = true, s5 = true)
            }
        }

        private fun setStarsIcon(s2: Boolean, s3: Boolean, s4: Boolean, s5: Boolean) {
            itemView.star2.visibility = if (s2) View.VISIBLE else View.GONE
            itemView.star3.visibility = if (s3) View.VISIBLE else View.GONE
            itemView.star4.visibility = if (s4) View.VISIBLE else View.GONE
            itemView.star5.visibility = if (s5) View.VISIBLE else View.GONE
        }
    }
}