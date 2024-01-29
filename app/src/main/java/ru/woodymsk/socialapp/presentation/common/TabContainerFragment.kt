package ru.woodymsk.socialapp.presentation.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.R.id.ftc_container
import ru.woodymsk.socialapp.R.layout.fragment_tab_container
import ru.woodymsk.socialapp.domain.getSerializableCompat
import ru.woodymsk.socialapp.domain.navigation.RouterProvider
import ru.woodymsk.socialapp.domain.navigation.subnavigation.LocalCiceroneHolder
import ru.woodymsk.socialapp.presentation.common.TabTag.AUTHORIZATION
import ru.woodymsk.socialapp.presentation.common.TabTag.EVENT_SCREEN
import ru.woodymsk.socialapp.presentation.common.TabTag.MY_PROFILE
import ru.woodymsk.socialapp.presentation.common.TabTag.POST_SCREEN
import javax.inject.Inject

@AndroidEntryPoint
class TabContainerFragment : Fragment(), RouterProvider, BackButtonListener {

    companion object {
        private const val BUNDLE_POST_KEY = "BUNDLE_POST_KEY"

        fun newInstance(containerTag: TabTag) =
            TabContainerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BUNDLE_POST_KEY, containerTag)
                }
            }
    }

    private val navigator: Navigator by lazy {
        AppNavigator(requireActivity(), ftc_container, childFragmentManager)
    }
    private val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(containerTag)
    private val containerTag: TabTag
        get() = requireArguments().getSerializableCompat(BUNDLE_POST_KEY, TabTag::class.java)
    override val router: Router
        get() = cicerone.router

    @Inject
    lateinit var ciceroneHolder: LocalCiceroneHolder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(fragment_tab_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (childFragmentManager.findFragmentById(ftc_container) == null) {
            when (containerTag) {
                POST_SCREEN -> {
                    router.replaceScreen(Screens.onPostScreenTab())
                }
                EVENT_SCREEN -> {
                    router.replaceScreen(Screens.onEventScreenTab())
                }
                AUTHORIZATION -> {
                    router.replaceScreen(Screens.onAuthScreenTab())
                }
                MY_PROFILE -> {
                    router.replaceScreen(Screens.onMyProfileScreenTab())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        cicerone.getNavigatorHolder().removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        val fragment = childFragmentManager.findFragmentById(ftc_container)
        if (fragment != null && fragment is BackButtonListener) {
            (fragment as BackButtonListener).onBackPressed()
        }
    }
}