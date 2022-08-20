package com.trdz.task14as_simplified.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.trdz.task14as_simplified.R
import com.trdz.task14as_simplified.base_utility.EFFECT_FADE
import com.trdz.task14as_simplified.base_utility.EFFECT_RISE
import com.trdz.task14as_simplified.base_utility.EFFECT_SLIDE

class Navigation(private var fastContainer: Int = 0) {

	fun returnTo(manager: FragmentManager, toId: Int = 0) {
		if (manager.backStackEntryCount <= toId) return
		val entry: FragmentManager.BackStackEntry = manager.getBackStackEntryAt(toId)
		manager.popBackStackImmediate(entry.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
	}

	fun add(manager: FragmentManager, fragment: Fragment?, remember: Boolean = true, container: Int = fastContainer,effect: String = "NONE") {
		if (manager.isDestroyed) return
		manager.beginTransaction().apply {
			when(effect) {
				EFFECT_RISE -> 	setCustomAnimations(
					R.anim.slide_up,
					R.anim.slide_down,
				)
				EFFECT_FADE -> 	setCustomAnimations(
					R.anim.fade_in,
					R.anim.fade_out,
				)
				EFFECT_SLIDE -> 	setCustomAnimations(
					R.anim.fade_in,
					R.anim.fade_out,
				)
			}
			add(container, fragment!!)
			if (remember) addToBackStack("")
			commit()
		}
	}

	fun replace(manager: FragmentManager, fragment: Fragment?, remember: Boolean = true, container: Int = fastContainer,effect: String = "NONE") {
		if (manager.isDestroyed) return
		manager.beginTransaction().apply {
			when(effect) {
				EFFECT_RISE -> 	setCustomAnimations(
					R.anim.slide_up,
					R.anim.slide_down,
				)
				EFFECT_FADE -> 	setCustomAnimations(
					R.anim.fade_in,
					R.anim.fade_out,
				)
				EFFECT_SLIDE -> 	setCustomAnimations(
					R.anim.fade_in,
					R.anim.fade_out,
				)
			}
			replace(container, fragment!!)
			setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
			if (remember) addToBackStack("")
			commit()
		}
	}


}