package com.trdz.task14as_simplified.model.server_pod

import com.trdz.task14as_simplified.model.server_pod.dto.ResponseDataPOD
import com.trdz.task14as_simplified.base_utility.PACKAGE_POD
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerRetrofitPodApi {
	@GET(PACKAGE_POD)
	fun getResponse(
		@Query("api_key") apiKey:String
	): Call<ResponseDataPOD>
}
interface ServerRetrofitPodCustomApi {
	@GET(PACKAGE_POD)
	fun getResponse(
		@Query("api_key") apiKey:String,
		@Query("date") date:String
	): Call<ResponseDataPOD>
}