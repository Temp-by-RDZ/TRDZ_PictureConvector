package com.trdz.task14as_simplified.model

import io.reactivex.rxjava3.core.Single

interface ServerResponse {
	fun response(data: Single<ServersResult>)
}