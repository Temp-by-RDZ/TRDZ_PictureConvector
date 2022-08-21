package com.trdz.task14as_simplified.base_utility

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.trdz.task14as_simplified.view.segment_picture.WindowPicture

object ScreenBase: FragmentScreen {
	override fun createFragment(factory: FragmentFactory): Fragment {
		return WindowPicture.newInstance()
	}
}