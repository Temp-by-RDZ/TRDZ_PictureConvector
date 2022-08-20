package com.trdz.task14as_simplified.model

import android.util.Log
import com.trdz.task14as_simplified.model.server_pod.ServerRetrofitPOD
import com.trdz.task14as_simplified.base_utility.PREFIX_POD
import io.reactivex.rxjava3.core.Single

class RepositoryExecutor: Repository {

	override fun connection(prefix: String, date: String?): Single<ServersResult> {
		Log.d("@@@", "Rep - start connection $prefix on date: $date")
		lateinit var dataSource: DataSource
		when (prefix) {
			PREFIX_POD -> dataSource = ServerRetrofitPOD()
		}
		return Single.create{
			it.onSuccess(dataSource.load(date))
			}
	}
}
