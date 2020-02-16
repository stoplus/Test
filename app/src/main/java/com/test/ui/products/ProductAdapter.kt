package com.test.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.test.R
import com.test.network.models.ProductModel
import com.test.utils.BASE_URL
import com.test.utils.IMAGE_PREFIX
import kotlinx.android.synthetic.main.item_product.view.*

class ProductAdapter(
    private val list: MutableList<ProductModel>,
    private val clickListener: (Int) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.updateItem(list[position])
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun updateItem(model: ProductModel) {
            Glide.with(itemView)
                .load(BASE_URL + IMAGE_PREFIX + model.img)
                .override(200, 200)
                .error(R.drawable.no_image)
                .placeholder(R.drawable.holder)
                .into(itemView.productImage)

            itemView.setOnClickListener { clickListener(model.id) }
            itemView.productTitle.text = model.title
            itemView.productText.text = model.description

            if (adapterPosition == itemCount - 1) {
                itemView.producSseporator.visibility = View.GONE
            }
        }
    }
}