package com.test.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.R
import com.test.base.BaseFragment
import com.test.network.models.ProductModel
import com.test.ui.MainViewModel
import com.test.utils.setMessage
import kotlinx.android.synthetic.main.fragment_product_list.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProductFragment : BaseFragment() {

    private val viewModel by sharedViewModel<MainViewModel>()

    companion object {
        const val TAG = "ProductFragment"
        fun newInstance() = ProductFragment()
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

        subscribe(
            viewModel.getProducts(), {
                initAdapter(it)
            }, {
                context?.also { con -> longToast(setMessage(it, con)) }
            })
    }

    private fun initAdapter(list: MutableList<ProductModel>) {
        productAdapter.adapter = ProductAdapter(list) { idProduct ->
            showFragment(
                DetailProductFragment.newInstance(idProduct),
                R.id.container_for_fragments,
                DetailProductFragment.TAG
            )
        }
    }

}