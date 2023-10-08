package ru.woodymsk.socialapp.presentation.post

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.R

@AndroidEntryPoint
class LoginDialogFragment : DialogFragment() {

    companion object {
        @JvmStatic
        val TAG = LoginDialogFragment::class.java.simpleName

        @JvmStatic
        val REQUEST_KEY = "$TAG:defaultRequestKey"

        @JvmStatic
        val KEY_RESPONSE = "RESPONSE"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val listener = DialogInterface.OnClickListener { _, which ->
            parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_RESPONSE to which))
        }

        return AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setTitle(getString(R.string.require_authorization))
            .setMessage(getString(R.string.execute_login))
            .setPositiveButton(getString(R.string.positive_answer), listener)
            .setNegativeButton(getString(R.string.negative_answer), listener)
            .create()
    }
}