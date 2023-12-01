package ru.woodymsk.socialapp.presentation.common

import android.app.Activity
import android.content.Intent
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import javax.inject.Inject

class PhotoImagePicker @Inject constructor() {

    companion object {
        private const val IMAGE_PNG = "image/png"
        private const val IMAGE_JPEG = "image/jpeg"
    }

    fun pickImage(activity: Activity, onResult: (Intent) -> Unit) {
        ImagePicker.with(activity)
            .crop()
            .compress(2048)
            .provider(ImageProvider.GALLERY)
            .galleryMimeTypes(
                arrayOf(
                    IMAGE_PNG,
                    IMAGE_JPEG,
                )
            )
            .createIntent(onResult)
    }

    fun takePhoto(activity: Activity, onResult: (Intent) -> Unit) {
        ImagePicker.with(activity)
            .crop()
            .compress(2048)
            .provider(ImageProvider.CAMERA)
            .createIntent(onResult)
    }
}