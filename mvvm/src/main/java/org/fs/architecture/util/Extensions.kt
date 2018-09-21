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

import android.view.LayoutInflater
import android.view.ViewGroup
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.fs.architecture.model.ViewModelType


val String.Companion.EMPTY get() = ""
val String.Companion.WHITE_SPACE get() = " "
val String.Companion.NEW_LINE get() = "\n"

fun <T> Observable<T>.async(): Observable<T> = observeOn(AndroidSchedulers.mainThread())
  .subscribeOn(Schedulers.io())

fun <T> Observable<T>.async(viewModel: ViewModelType?) = async()
  .doOnSubscribe { viewModel?.showProgress = true }
  .doFinally { viewModel?.showProgress = false }

fun <T> Single<T>.async(): Single<T> = observeOn(AndroidSchedulers.mainThread())
  .subscribeOn(Schedulers.io())

fun <T> Single<T>.async(viewModel: ViewModelType?) = async()
  .doOnSubscribe { viewModel?.showProgress = true }
  .doFinally { viewModel?.showProgress = false }

fun <T> Maybe<T>.async(): Maybe<T> = observeOn(AndroidSchedulers.mainThread())
  .subscribeOn(Schedulers.io())

fun <T> Maybe<T>.async(viewModel: ViewModelType?) = async()
  .doOnSubscribe { viewModel?.showProgress = true }
  .doFinally { viewModel?.showProgress = false }

fun <T> Flowable<T>.async(): Flowable<T> = observeOn(AndroidSchedulers.mainThread())
  .subscribeOn(Schedulers.io())

fun <T> Flowable<T>.async(viewModel: ViewModelType?) = async()
  .doOnSubscribe { viewModel?.showProgress = true }
  .doFinally { viewModel?.showProgress = false }

fun Completable.async(): Completable = subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())

fun Completable.async(viewModel: ViewModelType?): Completable = async()
  .doOnSubscribe { viewModel?.showProgress = true }
  .doFinally { viewModel?.showProgress = false }

fun ViewGroup.toFactory(): LayoutInflater = LayoutInflater.from(context)