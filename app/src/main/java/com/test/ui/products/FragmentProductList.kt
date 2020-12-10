package com.test.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.base.BaseFragment
import com.test.databinding.FragmentProductListBinding
import com.test.ui.MainViewModel
import com.test.utils.setMessage
import org.jetbrains.anko.support.v4.longToast

class FragmentProductList : BaseFragment<MainViewModel>(){

//    private val viewModel by sharedViewModel<MainViewModel>()
    private lateinit var adapterProd: ProductAdapter
    private var bindingNull: FragmentProductListBinding? = null
    private val binding get() = bindingNull!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingNull = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        getAllProducts()

        //init swipeRefresh
        binding.swipeContainer.setOnRefreshListener{ getAllProducts() }
        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)
    }

    private fun getAllProducts() {
        //get all products from api
        subscribe(
            viewModel.getProducts(), {
                adapterProd.updateList(it)
                binding.swipeContainer.isRefreshing = false
            }, {
                context?.also { con -> longToast(setMessage(it, con)) }
                if (binding.swipeContainer.isRefreshing){
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

    override fun onDestroyView() {
        super.onDestroyView()
        bindingNull = null
    }
}