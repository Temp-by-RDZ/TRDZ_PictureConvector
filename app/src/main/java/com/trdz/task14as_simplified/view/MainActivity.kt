package com.trdz.task14as_simplified.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.trdz.task14as_simplified.MyApp
import com.trdz.task14as_simplified.R
import com.trdz.task14as_simplified.base_utility.KEY_OPTIONS
import com.trdz.task14as_simplified.base_utility.KEY_THEME
import moxy.MvpAppCompatActivity

class MainActivity: MvpAppCompatActivity(), Leader {

	//region Elements
	private val navigation = Navigation(R.id.container_fragment_base)
	private val executor = Executor()
	private val navigator = AppNavigator(this,R.id.container_fragment_base)

	//endregion

	//region Customization

	override fun onBackPressed() {
		val fragmentList = supportFragmentManager.fragments

		var handled = false
		for (f in fragmentList) {
			if (f is CustomOnBackPressed) {
				handled = f.onBackPressed()
				if (handled) {
					break
				}
			}
		}

		if (!handled) super.onBackPressed()
	}

	//endregion

	//region Navigator
	override fun onResumeFragments() {
		super.onResumeFragments()
		MyApp.instance.navigationHandler.setNavigator(navigator)
	}

	override fun onPause() {
		MyApp.instance.navigationHandler.removeNavigator()
		super.onPause()
	}
	//endregion

	//region Base realization
	override fun onDestroy() {
		executor.stop()
		super.onDestroy()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		themeSettings()
		setContentView(R.layout.activity_main)
		if (savedInstanceState == null) {
			Log.d("@@@", "Start program")
			navigation.add(supportFragmentManager, WindowStart(), false, R.id.container_fragment_primal)
		}
	}

	private fun themeSettings() {
		when (getSharedPreferences(KEY_OPTIONS, Context.MODE_PRIVATE).getInt(KEY_THEME, 0)) {
			0 -> setTheme(R.style.MyBaseTheme)
			1 -> setTheme(R.style.MyGoldTheme)
			2 -> setTheme(R.style.MyFiolTheme)
		}
	}

	//endregion

	override fun getNavigation() = navigation
	override fun getExecutor() = executor

}