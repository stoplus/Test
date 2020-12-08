package com.test.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.R
import com.test.databinding.ItemProductBinding
import com.test.network.models.domain.ProductResult
import com.test.utils.BASE_URL
import com.test.utils.IMAGE_PREFIX

class ProductAdapter(
    private val clickListener: (ProductResult) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var list: MutableList<ProductResult> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        )
    }

    fun updateList(newList: MutableList<ProductResult>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.updateItem(list[position])
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemProductBinding.bind(itemView)

        fun updateItem(response: ProductResult) {
            Glide.with(binding.root)
                .load(BASE_URL + IMAGE_PREFIX + response.img)
                .override(200, 200)
                .error(R.drawable.no_image)
                .placeholder(R.drawable.holder)
                .into(binding.productImage)

            binding.root.setOnClickListener { clickListener(response) }
            binding.productTitle.text = response.title
            binding.productText.text = response.description

            if (adapterPosition == itemCount - 1) {
                binding.productSeparator.visibility = View.GONE
            }
        }
    }
}