package com.trdz.task14as_simplified.view.segment_picture

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView: MvpView {
	fun onSuccess(name: String, desc: String, url: String)
	fun onError(code: Int = -1, error: Throwable? = null)
	fun onSave()
	fun onConvert()
	fun onDone()
	fun loadingState(state: Boolean)
}