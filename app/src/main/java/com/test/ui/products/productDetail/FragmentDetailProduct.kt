package com.test.ui.products.productDetail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.test.R
import com.test.base.BaseFragment
import com.test.databinding.FragmentProductDetailBinding
import com.test.network.models.data.request.PostReviewRequest
import com.test.ui.products.RateAdapter
import com.test.utils.BASE_URL
import com.test.utils.IMAGE_PREFIX
import com.test.utils.clickBtn
import com.test.utils.setMessage
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import java.util.*

class FragmentDetailProduct : BaseFragment<ProductDetailViewModel>() {

//    private val viewModel by sharedViewModel<MainViewModel>()
    private val scope by lazy { AndroidLifecycleScopeProvider.from(this) }
    private var currentRate: Int = 0
    private var bindingNull: FragmentProductDetailBinding? = null
    private val binding get() = bindingNull!!

    override fun getParameters(): ParametersDefinition = {
        parametersOf(FragmentDetailProductArgs.fromBundle(requireArguments()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingNull = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        //for updating scope
        binding.includeMyComment.commentBtn.clickBtn(scope) { tryPostReview() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllReviews(false)//get all reviews from api

        viewModel.productLiveData.observe(viewLifecycleOwner, { productModel ->
            //set info about current product
            context?.also { cont ->
                Glide.with(cont)
                    .load(BASE_URL + IMAGE_PREFIX + productModel.img)
                    .override(200, 200)
                    .error(R.drawable.no_image)
                    .placeholder(R.drawable.holder)
                    .into(binding.productBarrier.productImage)
            }

            binding.productBarrier.productTitle.text = productModel.title
            binding.productBarrier.productText.text = productModel.description
        })

        initClicksStars()
        binding.includeMyComment.comment.requestFocus()
        binding.spExpand.setOnClickListener {
            //  show/gone list reviews
            if (binding.commentAdapter.visibility == View.VISIBLE) {
                binding.spExpand.animate().rotation(180F).start()
                binding.commentAdapter.visibility = View.GONE
            } else {
                binding.spExpand.animate().rotation(360F).start()
                TransitionManager.beginDelayedTransition(binding.group, ChangeBounds())
                binding.commentAdapter.visibility = View.VISIBLE
            }
        }
    }

    private fun getAllReviews(update: Boolean) {
        subscribe(
            viewModel.getReviews(), {
                binding.commentAdapter.adapter = RateAdapter(it)
                if (update) {
                    binding.scrollView.postDelayed(
                        { binding.scrollView.fullScroll(View.FOCUS_DOWN) },
                        500
                    )
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

    private fun showDialog() {
        val alertDialog = AlertDialog.Builder(binding.root.context)
        alertDialog.setTitle(R.string.comment_error_dialog_title)
        alertDialog.setMessage(R.string.comment_error_sing_in)
        alertDialog.setNegativeButton(android.R.string.cancel, null)
        alertDialog.setPositiveButton(R.string.sing_in) { _, _ -> router?.toLogin() }
        alertDialog.show()
    }

    private fun postReview() {
        subscribe(viewModel.postReview(
            PostReviewRequest(rate = currentRate, text = binding.includeMyComment.comment.text.toString())
        ), {
            if (it.success) {
                getAllReviews(true)
                toast(resources.getString(R.string.comment_posted))
            }
        }, {
            if (it.message != null && it.message!!.toLowerCase(Locale.getDefault())
                    .contains("unauthorized")
            ) {
                showDialog()
                viewModel.logout()
            } else {
                context?.also { con -> longToast(setMessage(it, con)) }
            }
        })
    }

    private fun initClicksStars() {
        binding.includeMyComment.starsInclude.star1.setOnClickListener { setStarsIcon(1, s2 = false, s3 = false, s4 = false, s5 = false) }
        binding.includeMyComment.starsInclude.star2.setOnClickListener { setStarsIcon(2, s2 = true, s3 = false, s4 = false, s5 = false) }
        binding.includeMyComment.starsInclude.star3.setOnClickListener { setStarsIcon(3, s2 = true, s3 = true, s4 = false, s5 = false) }
        binding.includeMyComment.starsInclude.star4.setOnClickListener { setStarsIcon(4, s2 = true, s3 = true, s4 = true, s5 = false) }
        binding.includeMyComment.starsInclude.star5.setOnClickListener { setStarsIcon(5, s2 = true, s3 = true, s4 = true, s5 = true) }
    }

    private fun setStarsIcon(rate: Int, s2: Boolean, s3: Boolean, s4: Boolean, s5: Boolean) {
        //change icon stars
        val image2 = if (s2) R.drawable.ic_star else R.drawable.ic_star_border
        val image3 = if (s3) R.drawable.ic_star else R.drawable.ic_star_border
        val image4 = if (s4) R.drawable.ic_star else R.drawable.ic_star_border
        val image5 = if (s5) R.drawable.ic_star else R.drawable.ic_star_border
        binding.includeMyComment.starsInclude.star1.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_star))
        binding.includeMyComment.starsInclude.star2.setImageDrawable(ContextCompat.getDrawable(binding.root.context, image2))
        binding.includeMyComment.starsInclude.star3.setImageDrawable(ContextCompat.getDrawable(binding.root.context, image3))
        binding.includeMyComment.starsInclude.star4.setImageDrawable(ContextCompat.getDrawable(binding.root.context, image4))
        binding.includeMyComment.starsInclude.star5.setImageDrawable(ContextCompat.getDrawable(binding.root.context, image5))
        currentRate = rate
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingNull = null
    }
}