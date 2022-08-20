package com.trdz.task14as_simplified.model.server_pod.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseDataPOD(
	val date: String,
	val explanation: String,
	val hdurl: String,
	@SerializedName("media_type")
	val mediaType: String,
	@SerializedName("service_version")
	val serviceVersion: String,
	val title: String,
	val url: String
	): Parcelable
