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
import retrofit2.Response

class ResultObservable<T>(private val stream: Observable<Response<T>>): Observable<Result<T>>() {

  override fun subscribeActual(observer: Observer<in Result<T>>?) {
    stream.subscribe(ResultObserver(observer))
  }

  class ResultObserver<R>(private val observer: Observer<in Result<R>>?): Observer<Response<R>> {

    override fun onComplete() {
      observer?.onComplete()
    }

    override fun onNext(t: Response<R>){
      observer?.onNext(Result.response(t))
    }

    override fun onError(e: Throwable) {
      try {
        observer?.onNext(Result.error(e))
      } catch (t: Throwable) {
        try {
          observer?.onError(t)
        } catch (inner: Throwable) {
          Exceptions.throwIfFatal(inner)
          RxJavaPlugins.onError(CompositeException(t, inner))
        }
        return
      }
      observer?.onComplete()
    }

    override fun onSubscribe(d: Disposable) {
      observer?.onSubscribe(d)
    }
  }
}