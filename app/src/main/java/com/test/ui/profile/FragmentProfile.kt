package com.test.ui.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.test.BuildConfig
import com.test.R
import com.test.base.BaseFragment
import com.test.databinding.FragmentProfileBinding
import com.test.network.models.domain.UserResult
import com.test.ui.login.ActivityLogin
import com.test.utils.withAllPermissions
import org.apache.commons.io.FileUtils
import org.jetbrains.anko.support.v4.toast
import java.io.File
import java.io.FileOutputStream

class FragmentProfile : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    private var currentPhotoPath = ""

    companion object {
        const val REQUEST_CODE_PHOTO = 100
        const val REQUEST_CODE_STORAGE = 222
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileAddPhotoBtn.setOnClickListener { choosePathForPhoto() }
        binding.profileSaveBtn.setOnClickListener { saveProfile() }
        binding.profileName.requestFocus()
        setDataUser() //set user data if it was saved earlier
    }

    private fun setDataUser() {
        val user = viewModel.getProfile()
        val uri: Uri = Uri.parse(user.photo)
        Glide.with(mContext).load(uri)
            .fitCenter()
            .circleCrop()
            .error(R.drawable.default_photo)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.profilePhoto)

        binding.profileName.setText(user.firstName)
        binding.profileSurname.setText(user.lastName)
    }

    private fun choosePathForPhoto() {
        //choose were we get image
        val alertDialog = AlertDialog.Builder(mContext)
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

    private fun saveProfile() {
        val user = viewModel.getProfile()
        if (binding.profileName.text.toString().isEmpty()
            || binding.profileSurname.text.toString().isEmpty()
            || (currentPhotoPath.isEmpty() && user.photo.isEmpty())
        ) {
            toast(resources.getString(R.string.profile_error_saved))
        } else {
            //save data user in preference
            viewModel.saveProfile(
                UserResult(
                    firstName = binding.profileName.text.toString(),
                    lastName = binding.profileSurname.text.toString(),
                    photo = if (currentPhotoPath.isEmpty()) user.photo else currentPhotoPath
                )
            )
            //exit from profile
            activity?.also {
                if (it is ActivityLogin) {
                    router?.toMain()
                } else {
                    onBackPressed()
                }
            }
            toast(resources.getString(R.string.profile_saved))
        }
    }

    private fun getImageFromGallery() =
        withAllPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) {
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
                    MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(
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
                        val uri = if (Build.VERSION.SDK_INT < 24) {
                            it
                        } else {
                            getRealPathURI(it)
                        }

                        currentPhotoPath = uri.toString()
                        showImage()
                    }
                }
            }
        }
    }

    private fun showImage() {
        Glide.with(mContext)
            .load(currentPhotoPath)
            .fitCenter()
            .circleCrop()
            .error(R.drawable.default_photo)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.profilePhoto)
    }

    private fun getRealPathURI(contentURI: Uri): Uri {
        val bitmap = getBitmapFromUri(contentURI)

        val folder = File(mContext.filesDir, "Images")
        folder.mkdirs()
        FileUtils.cleanDirectory(folder)

        val file = File.createTempFile("photo", ".jpg", folder)
        currentPhotoPath = "file:${file.absolutePath}"

        val fos = FileOutputStream(file)
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()

        return FileProvider.getUriForFile(
            binding.root.context,
            BuildConfig.APPLICATION_ID + ".provider",
            file
        )
    }

    private fun getBitmapFromUri(uri: Uri, options: BitmapFactory.Options? = null): Bitmap? {
        val parcelFileDescriptor = mContext.contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor?.fileDescriptor
        val image: Bitmap? = if (options != null) {
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)
        } else {
            BitmapFactory.decodeFileDescriptor(fileDescriptor)
        }
        parcelFileDescriptor?.close()
        return image
    }
}
