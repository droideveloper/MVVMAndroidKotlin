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
package org.fs.architecture.common

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import org.fs.architecture.model.EventType

class BusManager private constructor() {

  companion object {
    private val IMPL = BusManager()
    private val rxBus: PublishSubject<EventType> = PublishSubject.create<EventType>()

    fun <T> send(event: T) where T: EventType = IMPL.post(event)
    fun add(callback: Consumer<EventType>): Disposable = IMPL.register(callback)
    fun remove(disposable: Disposable) = IMPL.unregister(disposable)
  }

  fun <T> post(event: T) where T: EventType = rxBus.onNext(event)
  fun register(callback: Consumer<EventType>): Disposable = rxBus.subscribe(callback)
  fun unregister(disposable: Disposable) {
    if (!disposable.isDisposed) {
      disposable.dispose()
    }
  }
}