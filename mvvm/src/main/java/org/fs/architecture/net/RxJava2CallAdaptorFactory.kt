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

import io.reactivex.*
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class RxJava2CallAdaptorFactory(private val scheduler: Scheduler?, private val async: Boolean = true): CallAdapter.Factory() {

  companion object {
    fun create(): RxJava2CallAdaptorFactory = RxJava2CallAdaptorFactory(null, false)
    fun create(scheduler: Scheduler): RxJava2CallAdaptorFactory = RxJava2CallAdaptorFactory(scheduler, false)
    fun async(): RxJava2CallAdaptorFactory = RxJava2CallAdaptorFactory(null, true)
  }

  override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*, *> {
    val rawType = getRawType(returnType)
    if (rawType == Completable::class.java) {
      return RxJava2CallAdapter<Any>(Void::class.java, scheduler, async, false, true, false, false, false, true)
    }

    val flowable = rawType == Flowable::class.java
    val single = rawType == Single::class.java
    val maybe = rawType == Maybe::class.java

    if (rawType != Observable::class.java && !flowable && !single && !maybe) {
      throw IllegalStateException("you should be define return type as rx type")
    }

    if ((returnType !is ParameterizedType)) {
      val name: String
      if (flowable) {
        name = "Flowable"
      } else if (single) {
        name = "Single"
      } else if (maybe) {
        name = "Maybe"
      } else {
        name = "Observable"
      }
      throw IllegalStateException("$name return type must be parameterized as $name <Foo> or $name <? extends Foo>")
    }

    val observableType = getParameterUpperBound(0, returnType)
    val rawObservableType = getRawType(observableType)

    var result = false
    var body = false
    val responseType: Type
    if (rawObservableType == Response::class.java) {
      if (observableType !is ParameterizedType) {
        throw IllegalStateException("Response must be parameterize as Response<Foo> or Response<? extends Foo>")
      }
      responseType = getParameterUpperBound(0, observableType)
    } else if (rawObservableType == Result::class.java) {
      if (observableType !is ParameterizedType) {
        throw IllegalStateException("Response must be parameterize as Response<Foo> or Response<? extends Foo>")
      }
      responseType = getParameterUpperBound(0, observableType)
      result = true
    } else {
      responseType = observableType
      body = true
    }
    return RxJava2CallAdapter<Any>(responseType, scheduler, async, result, body, flowable, single, maybe, false)
  }
}