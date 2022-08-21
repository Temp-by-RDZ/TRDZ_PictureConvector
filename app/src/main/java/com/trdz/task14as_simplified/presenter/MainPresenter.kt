package com.trdz.task14as_simplified.presenter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import com.github.terrakok.cicerone.Router
import com.trdz.task14as_simplified.MyApp
import com.trdz.task14as_simplified.base_utility.*
import com.trdz.task14as_simplified.model.Repository
import com.trdz.task14as_simplified.model.RepositoryExecutor
import com.trdz.task14as_simplified.model.ServersResult
import com.trdz.task14as_simplified.view.segment_picture.MainView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MainPresenter(
	private val repository: Repository = RepositoryExecutor(),
	private val router: Router = MyApp.instance.router,
): MvpPresenter<MainView>() {

	private var repeat: Int = -1
	private var state = STATE_INI
	private lateinit var convertingProcess: Disposable

	override fun onFirstViewAttach() {
		super.onFirstViewAttach()
		connection()
	}

	private fun connection() {
		with(viewState) {
			loadingState(true)
			repository.connection(PREFIX_POD, getData(repeat))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
					{
						if (it.code in 200..299) {
							connectionDone(it)
						}
						else {
							onError(it.code)
							repeat--
							connection()
						}
						loadingState(false)
					},
					{ exception -> onError(-3, exception) })
		}
	}

	private fun getData(change: Int): String {
		val calendar = Calendar.getInstance()
		calendar.add(Calendar.DATE, change)
		val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
		return dateFormat.format(calendar.time)
	}

	private fun connectionDone(material: ServersResult) {
		viewState.onSuccess(material.name!!, material.description!!, material.url!!)
		savingPicture(material.url)
			.subscribeOn(Schedulers.io())
			.subscribe({
			state = STATE_SAVED
			viewState.onSave()
		}, { exception ->
			viewState.onError(-3, exception)
		})
	}

	private fun getDisc() = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Picture Convert")

	private fun savingPicture(url: String) =
		Completable.create {
			Log.d("@@@", "Prz - Start saving image")
			val file = getDisc()
			if (!file.exists() && !file.mkdirs()) { it.onError(Throwable(message = "Gallery not found")) }
			if (url == "") { it.onError(Throwable(message = "Image don't exist")) }
			else url.apply {
				val newFile = File("${file.absolutePath}/image.jpeg")
				Log.d("@@@", "Prz - preparing $newFile")
				try {
					val bitmap = getBitmapFromURL(this)
					val fOut = FileOutputStream(newFile)
					bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut)
					fOut.flush()
					fOut.close()
					Log.d("@@@", "Prz - Saving complete")
					it.onComplete()
				}
				catch (ignored: FileNotFoundException) {
					it.onError(Throwable(message = "File corrupted"))
				}
				catch (e: IOException) {
					it.onError(e)
				}
			}
		}

	private fun getBitmapFromURL(src: String): Bitmap {
		val url = URL(src)
		val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
		connection.doInput = true
		connection.connect()
		val input: InputStream = connection.inputStream
		return BitmapFactory.decodeStream(input)
	}

	fun needConversion() {
		when (state) {
			STATE_COVERT -> { abortConversion(); return }
			STATE_SAVED -> {
				viewState.onConvert()
				state = STATE_COVERT
				convertingProcess = convert()
					.delay(1, TimeUnit.SECONDS)
					.subscribe({
						state = STATE_DONE
						viewState.onDone()
					}, { exception ->
						viewState.onError(-3, exception)
					})
			}
		}
	}

	private fun abortConversion() {
		convertingProcess.dispose()
		viewState.onError(-3, Throwable(message = "User abort process"))
		state = 1
	}

	private fun convert() =
		Completable.create {
			Log.d("@@@", "Prz - Start converting image")
			val file = getDisc()
			if (!file.exists() && !file.mkdirs()) { it.onError(Throwable(message = "Gallery not found")) }
			val converted = File("${file.absolutePath}/converted.png")
			try {
				val bmp = BitmapFactory.decodeFile("${file.absolutePath}/image.jpeg")
				val out = FileOutputStream(converted)
				bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
				out.close()
				Log.d("@@@", "Prz - Converting complete")
				it.onComplete()
			}
			catch (e: Exception) {
				it.onError(Throwable(message = "Conversation Error ${e.message}"))
			}
		}
}