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
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.fs.architecture.model.AbstractViewModel
import org.fs.architecture.model.ViewModelType
import org.fs.architecture.model.ViewType
import javax.inject.Inject

abstract class AbstractActivity<VM>: AppCompatActivity() where VM: ViewModelType {

  @Inject lateinit var viewModel: VM
  lateinit var viewDataBinding: ViewDataBinding

  override fun onStart() {
    super.onStart()
    viewModel.attach()
  }

  override fun onStop() {
    viewModel.detach()
    super.onStop()
  }

  fun getStringResource(stringRes: Int): String? = getString(stringRes)

  fun isAvailable(): Boolean = !isFinishing

  fun getContext(): Context? = this

  fun view(): View? = findViewById(android.R.id.content)

  fun dismiss() = Unit
}