package ru.woodymsk.socialapp.presentation.common

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.presentation.post.LoginDialogFragment

private const val SDK_VERSION = 33

fun requestPermission(request: ActivityResultLauncher<String>, permissions: String) =
    request.launch(permissions)

fun Fragment.isPermissionGranted(permission: String) =
    ContextCompat.checkSelfPermission(
        requireContext(),
        permission,
    ) == PERMISSION_GRANTED

fun Fragment.permissionsRequireDialogFragment() =
    PermissionsRequiredDialogFragment().show(parentFragmentManager, LoginDialogFragment.TAG)

fun Fragment.checkPermissionResult(permission: String) {
    if (!shouldShowRequestPermissionRationale(permission)) {
        permissionsRequireDialogFragment()
    } else {
        Toast.makeText(requireContext(), R.string.permission_denied, Toast.LENGTH_SHORT)
            .show()
    }
}

fun getImagePermissionType(): String {
    return if (Build.VERSION.SDK_INT >= SDK_VERSION) {
        READ_MEDIA_IMAGES
    } else {
        READ_EXTERNAL_STORAGE
    }
}