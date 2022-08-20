package com.trdz.task14as_simplified

import android.app.Application
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.google.gson.GsonBuilder
import com.trdz.task14as_simplified.base_utility.DOMAIN
import com.trdz.task14as_simplified.model.server_pod.ServerRetrofitPodApi
import com.trdz.task14as_simplified.model.server_pod.ServerRetrofitPodCustomApi
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApp: Application() {

	companion object {
		lateinit var instance : MyApp
		private var retrofitPod: ServerRetrofitPodApi? = null
		private var retrofitPodCustom: ServerRetrofitPodCustomApi? = null

		private fun createRetrofit() {
			retrofitPod = Retrofit.Builder().apply {
				baseUrl(DOMAIN)
				addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
			}.build().create(ServerRetrofitPodApi::class.java)
		}

		fun getRetrofit(): ServerRetrofitPodApi {
			if (retrofitPod == null) createRetrofit()
			return retrofitPod!!
		}
		private fun createRetrofitCustom() {
			retrofitPodCustom = Retrofit.Builder().apply {
				baseUrl(DOMAIN)
				addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
			}.build().create(ServerRetrofitPodCustomApi::class.java)
		}

		fun getRetrofitCustom(): ServerRetrofitPodCustomApi {
			if (retrofitPodCustom == null) createRetrofitCustom()
			return retrofitPodCustom!!
		}
	}

	private val cicerone : Cicerone<Router> by lazy { Cicerone.create() }

	val navigationHandler = cicerone.getNavigatorHolder()
	val router = cicerone.router

	override fun onCreate() {
		super.onCreate()
		instance = this
		RxJavaPlugins.setErrorHandler {
			//None
		}
	}

}