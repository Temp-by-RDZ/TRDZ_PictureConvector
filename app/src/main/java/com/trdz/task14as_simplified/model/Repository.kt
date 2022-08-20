package com.trdz.task14as_simplified.model

import io.reactivex.rxjava3.core.Single

interface Repository {
	fun connection(prefix: String, date: String?): Single<ServersResult>
}