package com.trdz.task14as_simplified.model

interface DataSource {
	fun load(date: String?):ServersResult
}