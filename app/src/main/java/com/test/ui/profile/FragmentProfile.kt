package com.test.ui.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.test.BuildConfig
import com.test.R
import com.test.base.BaseFragment
import com.test.network.models.UserModel
import com.test.ui.MainViewModel
import com.test.utils.clickBtn
import com.test.utils.withAllPermissions
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import kotlinx.android.synthetic.main.container_for_activity.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File

class FragmentProfile : BaseFragment() {

    private val scope by lazy { AndroidLifecycleScopeProvider.from(this) }
    private val viewModel by sharedViewModel<MainViewModel>()
    private var currentPhotoPath = ""

    companion object {
        const val REQUEST_CODE_PHOTO = 100
        const val REQUEST_CODE_STORAGE = 222
        const val TAG = "FragmentProfile"
        fun newInstance() = FragmentProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onResume() {
        super.onResume()
        //for updating scope
        profileAddPhotoBtn.clickBtn(scope) { choosePathForPhoto() }
        profileSaveBtn.clickBtn(scope) { saveProfile() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.also {
            it.include_toolbar.visibility = View.GONE
            profile_name.requestFocus()
        }

        setDataUser() //set user data if it was saved earlier
    }

    private fun setDataUser() {
        val user = viewModel.getProfile()
        val uri: Uri = Uri.parse(user.photo)
        context?.also {
            Glide.with(it).load(uri)
                .fitCenter()
                .circleCrop()
                .error(R.drawable.default_photo)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(profilePhoto)
        }
        profile_name.setText(user.firstName)
        profileSurname.setText(user.lastName)
    }

    private fun choosePathForPhoto() {
        //choose were we get image
        context?.also {
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setTitle(resources.getString(R.string.dialog_choose_title))
            alertDialog.setMessage(resources.getString(R.string.dialog_choose_text))
                .setIcon(R.drawable.question)
                .setNegativeButton(resources.getString(R.string.dialog_choose_gallery_btn)) { dialog, _ ->
                    getImageFromGallery()
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.dialog_choose_camera_btn)) { dialog, _ ->
                    makePhoto()
                    dialog.dismiss()
                }
            alertDialog.show()
        }
    }

    private fun saveProfile() {
        if (profile_name.text.toString().isEmpty() && profileSurname.text.toString().isEmpty() && currentPhotoPath.isEmpty()) {
            toast(resources.getString(R.string.profile_error_saved))
        } else {
            //save data user in preference
            viewModel.saveProfile(
                UserModel(
                    firstName = profile_name.text.toString(),
                    lastName = profileSurname.text.toString(),
                    photo = currentPhotoPath
                )
            )
            toast(resources.getString(R.string.profile_saved))
        }
    }

    private fun getImageFromGallery() =
        withAllPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE) {
            startActivityForResult(
                Intent(Intent.ACTION_PICK).apply { type = "image/*" },
                REQUEST_CODE_STORAGE
            )
        }

    private fun makePhoto() =
        withAllPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE) {
            val activity = activity ?: return@withAllPermissions
            val file = File.createTempFile("photo", ".jpg", activity.filesDir)
            currentPhotoPath = "file:${file.absolutePath}"
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(
                    MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                        activity,
                        BuildConfig.APPLICATION_ID + ".provider",
                        file
                    )
                )
            }, REQUEST_CODE_PHOTO)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PHOTO -> showImage()
                REQUEST_CODE_STORAGE -> {
                    data?.data?.also {
                        val photoFile = File(getRealPathFromURI(it))
                        val uri = FileProvider.getUriForFile(
                            context!!, BuildConfig.APPLICATION_ID + ".provider",
                            photoFile
                        )
                        currentPhotoPath = uri.toString()
                        showImage()
                    }
                }
            }
        }
    }

    private fun showImage() {
        context?.also {
            Glide.with(it).load(currentPhotoPath)
                .fitCenter()
                .circleCrop()
                .error(R.drawable.default_photo)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(profilePhoto)
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): String {
        var result = ""
        var cursor: Cursor? = null
        activity?.also {
            cursor = it.contentResolver.query(contentURI, null, null, null, null)
        }
        cursor?.also {
            it.moveToFirst()
            result = it.getString(it.getColumnIndex(MediaStore.Files.FileColumns.DATA))
            it.close()
        }
        return result
    }
}
