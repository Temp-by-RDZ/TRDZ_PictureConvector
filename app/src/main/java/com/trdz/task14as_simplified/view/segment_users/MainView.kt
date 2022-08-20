package com.trdz.task14as_simplified.view.segment_users

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.lang.Exception

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView: MvpView {
	fun onSuccess(name: String, desc: String, url: String)
	fun onError(code: Int = -1, error: Throwable? = null)
	fun loadingState(state: Boolean)
}