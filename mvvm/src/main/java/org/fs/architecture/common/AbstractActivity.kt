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

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import org.fs.architecture.model.ViewModelType
import javax.inject.Inject

abstract class AbstractActivity<VM>: AppCompatActivity() where VM: ViewModelType {

  @Inject lateinit var viewModel: VM
  private lateinit var viewDataBinding: ViewDataBinding

  protected abstract val layoutRes: Int
  protected abstract val BRviewModel: Int

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    viewDataBinding = DataBindingUtil.setContentView(this, layoutRes)
    viewDataBinding.setVariable(BRviewModel, viewModel)
    setUp(savedInstanceState ?: intent.extras)
  }

  override fun onStart() {
    super.onStart()
    attach()
    viewModel.attach()
  }

  override fun onStop() {
    detach()
    viewModel.detach()
    super.onStop()
  }

  abstract fun setUp(state: Bundle?)
  abstract fun attach()
  abstract fun detach()

  open fun stringResource(stringRes: Int): String? = getString(stringRes)
  open fun isAvailable(): Boolean = !isFinishing
  open fun context(): Context? = this
  open fun dismiss() = Unit
}