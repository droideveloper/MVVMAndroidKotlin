/*
 * MVI Kotlin Copyright (C) 2018 Fatih.
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

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import org.fs.architecture.model.ViewModelType
import javax.inject.Inject

abstract class AbstractBottomSheetDialogFragment<VM>: BottomSheetDialogFragment() where VM: ViewModelType {

  @Inject lateinit var viewModel: VM
  private lateinit var viewDataBinding: ViewDataBinding

  protected abstract val layoutRes: Int
  protected abstract val BRviewModel: Int

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    viewDataBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
    return viewDataBinding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    AndroidSupportInjection.inject(this)
    super.onActivityCreated(savedInstanceState)
    viewDataBinding.setVariable(BRviewModel, viewModel)
    setUp(savedInstanceState ?: arguments)
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

  override fun show(manager: FragmentManager?, tag: String?) {
    if (manager != null) {
      show(manager.beginTransaction(), tag)
    }
  }

  override fun show(transaction: FragmentTransaction?, tag: String?): Int {
    if (transaction != null) {
      return transaction.add(this, tag)
          .commit()
    }
    return -1
  }

  abstract fun setUp(state: Bundle?)
  abstract fun attach()
  abstract fun detach()

  fun supportFragmentManager(): FragmentManager = childFragmentManager
  fun stringResource(stringRes: Int): String? = getString(stringRes)
  fun isAvailable(): Boolean = isAdded && activity != null
  fun finish() = Unit
}