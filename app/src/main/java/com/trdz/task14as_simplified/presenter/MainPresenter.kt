package com.trdz.task14as_simplified.presenter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import com.github.terrakok.cicerone.Router
import com.trdz.task14as_simplified.MyApp
import com.trdz.task14as_simplified.base_utility.PREFIX_POD
import com.trdz.task14as_simplified.model.Repository
import com.trdz.task14as_simplified.model.RepositoryExecutor
import com.trdz.task14as_simplified.view.segment_users.MainView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.InjectViewState
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

	var repeat: Int =-1

	override fun onFirstViewAttach() {
		super.onFirstViewAttach()
		connection()
	}

	private fun connection() {
		with(viewState) {
			loadingState(true)
			repository.connection(PREFIX_POD,getData(repeat))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
					{
						if (it.code in 200..299 ) {
							onSuccess(it.name!!,it.description!!,it.url!!)
							thread {
								savingPicture(it.url)
							}.start()
						}
						else {
							onError(it.code)
							repeat--
							connection()
						}
						loadingState(false)
					},
					{ exception-> onError(-2,exception) })
		}
	}

	private fun getData(change: Int): String {
		val calendar = Calendar.getInstance()
		calendar.add(Calendar.DATE, change)
		val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
		return dateFormat.format(calendar.time)
	}

	private fun getDisc()= File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Picture Convert")

	private fun savingPicture(url: String)  {
		Log.d("@@@", "Prz - Start saving image")
		val file = getDisc()
		if (!file.exists() && !file.mkdirs()) {
			Log.d("@@@", "Prz - Gallery not found");return
		}
		if (url=="") {
			Log.d("@@@", "Prz - Image don't exist");return
		}
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
				viewState.onSave()
			}
			catch (ignored: Exception) {
				Log.d("@@@", "Prz - File corrupted")
			}
			catch (e: IOException) {
				Log.d("@@@", e.message.toString())
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

	fun convert() {
		Log.d("@@@", "Prz - Start converting image")
		val file = getDisc()
		if (!file.exists() && !file.mkdirs()) {
			Log.d("@@@", "Prz - Gallery not found");return
		}
		val converted = File("${file.absolutePath}/converted.png")
		try {
			val bmp = BitmapFactory.decodeFile("${file.absolutePath}/image.jpeg")
			val out = FileOutputStream(converted)
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out) //100-best quality
			out.close()
			Log.d("@@@", "Prz - Converting complete")
			viewState.onDone()
		}
		catch (e: Exception) {
			Log.d("@@@", "Prz - File corrupted")
			e.printStackTrace()
		}
	}
}