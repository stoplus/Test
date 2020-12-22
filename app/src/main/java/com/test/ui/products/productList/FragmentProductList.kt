package com.test.ui.products.productList

import android.os.Bundle
import android.view.View
import com.test.base.BaseFragment
import com.test.databinding.FragmentProductListBinding
import com.test.utils.setMessage
import org.jetbrains.anko.support.v4.longToast

class FragmentProductList : BaseFragment<ProductListViewModel, FragmentProductListBinding>() {

    private lateinit var adapterProd: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        getAllProducts()

        //init swipeRefresh
        binding.swipeContainer.setOnRefreshListener { getAllProducts() }
        binding.swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    private fun getAllProducts() {
        //get all products from api
        subscribe(
            viewModel.getProducts(), {
                adapterProd.updateList(it)
                binding.swipeContainer.isRefreshing = false
            }, {
                longToast(setMessage(it, mContext))
                if (binding.swipeContainer.isRefreshing) {
                    binding.swipeContainer.isRefreshing = false
                }
            }
        )
    }

    private fun initAdapter() {
        adapterProd = ProductAdapter { product ->
            router?.navigate(
                FragmentProductListDirections.actionFragmentProductListToFragmentDetailProduct(
                    product
                )
            )
        }
        binding.productAdapter.adapter = adapterProd
    }
}