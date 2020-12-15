package com.test.ui.products.productList

import com.test.base.BaseViewModel
import com.test.network.models.domain.ProductResult
import com.test.ui.products.ProductUseCase
import io.reactivex.Single

abstract class ProductListViewModel : BaseViewModel() {
    abstract fun getProducts(): Single<MutableList<ProductResult>>
}

class ProductListViewModelImpl(
    private val productUseCase: ProductUseCase
) : ProductListViewModel() {

    override fun getProducts(): Single<MutableList<ProductResult>> {
        return productUseCase.getProducts()
    }
}