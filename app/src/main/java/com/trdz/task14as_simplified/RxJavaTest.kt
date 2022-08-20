package com.trdz.task14as_simplified

import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.lang.Exception

val data = listOf(1, 2, 3, 4, 5, 6, 7, 7, 8, 9)

fun main() {
	firs()
	second()
}

fun firs() {
	val observer = object: Observer<Int> {
		override fun onSubscribe(d: Disposable?) {
			println("Viewer was subscribed")
		}

		override fun onNext(t: Int?) {
			println("Viewer see next $t")
		}

		override fun onError(e: Throwable?) {
			println("Observable has an Error")
		}

		override fun onComplete() {
			println("Observe complete\n")
		}
	}
	val observable = Observable.create<Int> { emmiter ->
		try {
			data.forEach { element ->
				emmiter.onNext(element)
			}

		}
		catch (e: Exception) {
			emmiter.onError(e)
		}
		emmiter.onComplete()
	}
	observable.subscribe(observer)
}

fun second() {
	val bag = CompositeDisposable()

	Observable.fromIterable(data)
		.switchMap {
			Observable.just("$it.")
			//.delay(1, TimeUnit.SECONDS)
		}
		.distinctUntilChanged()
		.map(::secondTransform)
		.map { mutableListOf(it) }
		.map { it.add(listOf("!")); return@map it }
		//.delay(10000,TimeUnit.MILLISECONDS)
		//.subscribeOn(Schedulers.io())
		//.observeOn(AndroidSchedulers.mainThread())
		.subscribe(
			{ element -> println(element) },
			{ error -> println("Get an Error: $error") })
		.disposeBy(bag)
	//bag.dispose()

	/**
	 * true - Включение задержки для делея имитирующего асинхорнную работу
	 */
	while (false) {
		//Sleeping
	}
}

fun <Type> secondTransform(value: Type): List<String> {
	return listOf(value.toString(), "<-Значение")
}

private fun Disposable.disposeBy(bag: CompositeDisposable) {
	bag.add(this)
}

//region Other:
// map			- Обработка элементов потока
// zip			- Паралельная обработка потоков
// flatMap 		- Обработка потоков
// concatMap 	- Обработка потоков с сохранением очереди в случае ассинхронного обращения
// switchMap	- В случае асинхронного обращения обрабатывает только последний пришедший поток
// Single 		- Поток из одного элемента
// Maybe  		- Поток из одного возможного элемента
// Completable  - Поток без возврата элементов
// Flowable 	- Поток с защитой от перегрузки