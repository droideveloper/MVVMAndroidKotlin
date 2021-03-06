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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallEnqueuObservable<T>(private val call: Call<T>?): Observable<Response<T>>(){

  override fun subscribeActual(observer: Observer<in Response<T>>?) {
    val c = call?.clone()

    if (c != null) {
      val callback = CallCallback(c, observer)
      observer?.onSubscribe(callback)
      c.enqueue(callback)
    }
  }


  class CallCallback<R>(private val call: Call<*>, private val observer: Observer<in Response<R>>?, var terminated: Boolean = false): Disposable, Callback<R> {

    override fun isDisposed(): Boolean = call.isCanceled
    override fun dispose() = call.cancel()

    override fun onFailure(call: Call<R>?, t: Throwable) {
      if (call?.isCanceled == true) return

      try {
        observer?.onError(t)
      } catch (inner: Throwable) {
        Exceptions.throwIfFatal(inner)
        RxJavaPlugins.onError(CompositeException(t, inner))
      }
    }

    override fun onResponse(call: Call<R>?, response: Response<R>) {
      if (call?.isCanceled == true) return

      try {
        observer?.onNext(response)
        if (call?.isCanceled != true) {
          terminated = true
          observer?.onComplete()
        }
      } catch (t: Throwable) {
        if (terminated) {
          RxJavaPlugins.onError(t)
        } else if ((call?.isCanceled != true)) {
          try {
            observer?.onError(t)
          } catch (inner: Throwable) {
            Exceptions.throwIfFatal(inner)
            RxJavaPlugins.onError(CompositeException(t, inner))
          }
        }
      }
    }
  }
}