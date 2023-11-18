package ru.woodymsk.socialapp.presentation.common

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.DialogFragment
import com.google.android.datatransport.runtime.scheduling.persistence.EventStoreModule_PackageNameFactory
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.R

@AndroidEntryPoint
class PermissionsRequiredDialogFragment : DialogFragment() {

    companion object {
        private const val URI_PACKAGE_SCHEME = "package"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts(
                URI_PACKAGE_SCHEME,
                EventStoreModule_PackageNameFactory.packageName(requireContext()), null
            )
        )

        val listener = DialogInterface.OnClickListener { _, _ ->
            startActivity(appSettingsIntent)
        }

        return AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setTitle(getString(R.string.permission_denied))
            .setMessage(getString(R.string.permission_must_be_granted))
            .setPositiveButton(getString(R.string.positive_answer), listener)
            .setNegativeButton(getString(R.string.negative_answer)) { _, _ -> }
            .create()
    }
}