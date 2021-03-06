package com.test.ui.products

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.test.R
import com.test.base.BaseFragment
import com.test.network.models.request.PostReviewRequest
import com.test.ui.MainViewModel
import com.test.ui.login.ActivityLogin
import com.test.utils.BASE_URL
import com.test.utils.IMAGE_PREFIX
import com.test.utils.clickBtn
import com.test.utils.setMessage
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import kotlinx.android.synthetic.main.container_for_activity.*
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.view_for_my_comment.*
import kotlinx.android.synthetic.main.view_product.*
import kotlinx.android.synthetic.main.view_stars.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class FragmentDetailProduct : BaseFragment() {

    private val viewModel by sharedViewModel<MainViewModel>()
    private val scope by lazy { AndroidLifecycleScopeProvider.from(this) }
    private val idProduct by lazy { arguments!!.getInt(PRODUCT_ID) }
    private var currentRate: Int = 0

    companion object {
        const val TAG = "FragmentDetailProduct"
        const val PRODUCT_ID = "idProduct"
        fun newInstance(idProduct: Int) = FragmentDetailProduct().apply {
            arguments = Bundle().apply {
                putInt(PRODUCT_ID, idProduct)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onResume() {
        super.onResume()
        //for updating scope
        commentBtn.clickBtn(scope) { tryPostReview() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.also { it.include_toolbar.visibility = View.VISIBLE }
        getAllReviews(false)//get all reviews from api

        viewModel.productsList.observe(this, Observer {
            //set info about current product
            it.filter { model -> model.id == idProduct }
                .map { productModel ->
                    context?.also { cont ->
                        Glide.with(cont)
                            .load(BASE_URL + IMAGE_PREFIX + productModel.img)
                            .override(200, 200)
                            .error(R.drawable.no_image)
                            .placeholder(R.drawable.holder)
                            .into(productImage)
                    }

                    productTitle.text = productModel.title
                    productText.text = productModel.description
                }
        })

        initClicksStars()
        comment.requestFocus()
        sp_expand.setOnClickListener {
            //  show/gone list reviews
            if (commentAdapter.visibility == View.VISIBLE) {
                sp_expand.animate().rotation(180F).start()
                commentAdapter.visibility = View.GONE
            } else {
                sp_expand.animate().rotation(360F).start()
                TransitionManager.beginDelayedTransition(group, ChangeBounds())
                commentAdapter.visibility = View.VISIBLE
            }
        }
    }

    private fun getAllReviews(update: Boolean) {
        subscribe(
            viewModel.getReviews(idProduct), {
                commentAdapter.adapter = RateAdapter(it)
                if (update) {
                    scroll_view.postDelayed({ scroll_view.fullScroll(View.FOCUS_DOWN) }, 500)
                }
            }, { context?.also { con -> longToast(setMessage(it, con)) } })
    }

    private fun tryPostReview() {
        //check before post review
        if (!viewModel.isLogged()) {
            showDialog()
        } else if (currentRate == 0) {
            toast(resources.getString(R.string.comment_error_rate))
        } else {
            postReview()
        }
    }

    private fun showDialog(){
        context?.also {
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setTitle(R.string.comment_error_dialog_title)
            alertDialog.setMessage(it.resources.getString(R.string.comment_error_sing_in))
            alertDialog.setNegativeButton(android.R.string.cancel, null)
            alertDialog.setPositiveButton(it.resources.getString(R.string.sing_in)) { _, _ ->
                activity?.also { act -> ActivityLogin.start(act) }
            }
            alertDialog.show()
        }
    }

    private fun postReview() {
        subscribe(viewModel.postReview(
            PostReviewRequest(rate = currentRate, text = comment.text.toString())
            , idProduct
        ), {
            if (it.success) {
                getAllReviews(true)
                toast(resources.getString(R.string.comment_posted))
            }
        }, {
            if(it.message!=null && it.message!!.toLowerCase(Locale.getDefault()).contains("unauthorized")){
                showDialog()
                viewModel.logout()
            }else{
                context?.also { con -> longToast(setMessage(it, con)) }
            }
        })
    }

    private fun initClicksStars() {
        star1.setOnClickListener { setStarsIcon(1, s2 = false, s3 = false, s4 = false, s5 = false) }
        star2.setOnClickListener { setStarsIcon(2, s2 = true, s3 = false, s4 = false, s5 = false) }
        star3.setOnClickListener { setStarsIcon(3, s2 = true, s3 = true, s4 = false, s5 = false) }
        star4.setOnClickListener { setStarsIcon(4, s2 = true, s3 = true, s4 = true, s5 = false) }
        star5.setOnClickListener { setStarsIcon(5, s2 = true, s3 = true, s4 = true, s5 = true) }
    }

    private fun setStarsIcon(rate: Int, s2: Boolean, s3: Boolean, s4: Boolean, s5: Boolean) {
        //change icon stars
        val image2 = if (s2) R.drawable.ic_star else R.drawable.ic_star_border
        val image3 = if (s3) R.drawable.ic_star else R.drawable.ic_star_border
        val image4 = if (s4) R.drawable.ic_star else R.drawable.ic_star_border
        val image5 = if (s5) R.drawable.ic_star else R.drawable.ic_star_border
        star1.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_star))
        star2.setImageDrawable(ContextCompat.getDrawable(context!!, image2))
        star3.setImageDrawable(ContextCompat.getDrawable(context!!, image3))
        star4.setImageDrawable(ContextCompat.getDrawable(context!!, image4))
        star5.setImageDrawable(ContextCompat.getDrawable(context!!, image5))
        currentRate = rate
    }
}