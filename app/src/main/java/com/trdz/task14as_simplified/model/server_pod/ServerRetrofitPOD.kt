package com.trdz.task14as_simplified.model.server_pod

import android.util.Log
import com.trdz.task14as_simplified.BuildConfig
import com.trdz.task14as_simplified.MyApp
import com.trdz.task14as_simplified.model.DataSource
import com.trdz.task14as_simplified.model.ServersResult
import com.trdz.task14as_simplified.model.server_pod.dto.ResponseDataPOD
import retrofit2.Response

class ServerRetrofitPOD: DataSource {

	override fun load(date: String?): ServersResult {
		if (date!=null) return loadCustom(date)
		val retrofit = MyApp.getRetrofit()

		return try {
			val response = retrofit.getResponse(BuildConfig.NASA_API_KEY).execute()
			responseFormation(response)
		}
		catch (Ignored: Exception) {
			responseFail()
		}
	}

	private fun loadCustom(date: String): ServersResult {
		val retrofit = MyApp.getRetrofitCustom()

		return try {
			val response = retrofit.getResponse(BuildConfig.NASA_API_KEY,date).execute()
			responseFormation(response)
		}
		catch (Ignored: Exception) {
			responseFail()
		}
	}

	private fun responseFormation(response: Response<ResponseDataPOD>) : ServersResult {
		return if (response.isSuccessful) response.body()!!.run {
			ServersResult(response.code(), title, explanation, url, mediaType)
		}
		else ServersResult(response.code())
	}

	private fun responseFail() : ServersResult {
		Log.d("@@@", "Ser - POD Connection Error")
		return ServersResult(-1)
	}
}
