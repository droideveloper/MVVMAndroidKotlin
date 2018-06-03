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
package org.fs.architecture.net

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.exceptions.Exceptions
import io.reactivex.plugins.RxJavaPlugins
import retrofit2.HttpException
import retrofit2.Response


class BodyObservable<T>(private val stream: Observable<Response<T>>): Observable<T>() {

  override fun subscribeActual(observer: Observer<in T>?) {
    stream.subscribe(BodyObserver(observer))
  }

  class BodyObserver<R>(private val observer: Observer<in R>?, var terminated: Boolean = false): Observer<Response<R>> {

    override fun onSubscribe(d: Disposable) {
      observer?.onSubscribe(d)
    }

    override fun onComplete() {
      if (!terminated) {
        observer?.onComplete()
      }
    }

    override fun onNext(response: Response<R>) {
      if (response.isSuccessful) {
        val body = response.body()
        body?.let {
          observer?.onNext(it)
        }
      } else {
        terminated = true
        val t = HttpException(response)
        try {
          observer?.onError(t)
        } catch (inner: Throwable) {
          Exceptions.throwIfFatal(inner)
          RxJavaPlugins.onError(CompositeException(t, inner))
        }
      }
    }

    override fun onError(e: Throwable) {
      if (!terminated) {
        observer?.onError(e)
      } else {
        val t = AssertionError("This should never happen! Report as a bug with the full stacktrace.")
        t.initCause(e)
        RxJavaPlugins.onError(t)
      }
    }
  }
}