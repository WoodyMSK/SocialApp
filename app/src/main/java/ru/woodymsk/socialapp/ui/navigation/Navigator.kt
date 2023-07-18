package ru.woodymsk.socialapp.ui.navigation

import androidx.fragment.app.Fragment

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {

    fun navigateTo(fragment: Fragment, addToBackStack: Boolean = true)

}