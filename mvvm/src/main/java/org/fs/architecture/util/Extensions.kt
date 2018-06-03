/*
 * MVVM Android Kotlin Copyright (C) 2017 Fatih.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fs.architecture.util

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.fs.architecture.model.ViewType
import java.io.PrintWriter
import java.io.StringWriter

val String.Companion.EMPTY get() = ""
val String.Companion.WHITE_SPACE get() = " "
val String.Companion.NEW_LINE get() = "\n"

fun <T: Any> T.isLogEnabled(): Boolean = false

fun <T: Any> T.getClassTag(): String {
  val klazz = this.javaClass
  return klazz.simpleName
}

fun <T: Any> T.log(msg: String) {
  log(Log.DEBUG, msg)
}

fun <T: Any> T.log(error: Throwable) {
  val str = StringWriter()
  val ptr = PrintWriter(str)
  error.printStackTrace(ptr)
  log(Log.ERROR, str.toString())
}

fun <T: Any> T.log(level: Int, msg: String) {
  if (isLogEnabled()) {
    Log.println(level, getClassTag(), msg)
  }
}

fun <T> Observable<T>.async(): Observable<T> = this.observeOn(AndroidSchedulers.mainThread())
  .subscribeOn(Schedulers.io())

fun <T> Observable<T>.async(viewType: ViewType) = this.async()
  .doOnSubscribe { viewType.showProgress() }
  .doFinally { viewType.hideProgress() }

fun <T> Single<T>.async(): Single<T> = this.observeOn(AndroidSchedulers.mainThread())
  .subscribeOn(Schedulers.io())

fun <T> Single<T>.async(viewType: ViewType) = this.async()
  .doOnSubscribe { viewType.showProgress() }
  .doFinally { viewType.hideProgress() }

fun <T> Maybe<T>.async(): Maybe<T> = this.observeOn(AndroidSchedulers.mainThread())
  .subscribeOn(Schedulers.io())

fun <T> Maybe<T>.async(viewType: ViewType) = this.async()
  .doOnSubscribe { viewType.showProgress() }
  .doFinally { viewType.hideProgress() }

fun <T> Flowable<T>.async(): Flowable<T> = this.observeOn(AndroidSchedulers.mainThread())
  .subscribeOn(Schedulers.io())

fun <T> Flowable<T>.async(viewType: ViewType) = this.async()
  .doOnSubscribe { viewType.showProgress() }
  .doFinally { viewType.hideProgress() }
