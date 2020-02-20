package com.test.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.R
import com.test.base.BaseFragment
import com.test.ui.MainViewModel
import com.test.utils.setMessage
import kotlinx.android.synthetic.main.container_for_activity.*
import kotlinx.android.synthetic.main.fragment_product_list.*
import org.jetbrains.anko.support.v4.longToast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FragmentProductList : BaseFragment(){

    private val viewModel by sharedViewModel<MainViewModel>()
    private lateinit var adapterProd: ProductAdapter

    companion object {
        const val TAG = "FragmentProductList"
        fun newInstance() = FragmentProductList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.also { it.include_toolbar.visibility = View.VISIBLE }

        initAdapter()
        getAllProducts()

        //init swipeRefresh
        swipeContainer.setOnRefreshListener{
            getAllProducts()
        }
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)
    }

    private fun getAllProducts() {
        //get all products from api
        subscribe(
            viewModel.getProducts(), {
                adapterProd.updateList(it)
                swipeContainer.isRefreshing = false
            }, {
                context?.also { con -> longToast(setMessage(it, con)) }
                if (swipeContainer.isRefreshing){
                    swipeContainer.isRefreshing = false
                }
            }
        )
    }

    private fun initAdapter() {
        adapterProd = ProductAdapter { idProduct ->
            showFragment(
                FragmentDetailProduct.newInstance(idProduct),
                R.id.container_for_fragments,
                FragmentDetailProduct.TAG
            )
        }
        productAdapter.adapter = adapterProd
    }
}