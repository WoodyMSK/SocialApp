package ru.woodymsk.socialapp.domain

import androidx.fragment.app.Fragment

interface Navigator {

    fun navigateTo(fragment: Fragment, addToBackStack: Boolean = true)

}