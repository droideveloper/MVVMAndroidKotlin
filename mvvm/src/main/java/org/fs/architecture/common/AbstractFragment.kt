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

import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import org.fs.architecture.model.AbstractViewModel
import org.fs.architecture.model.ViewType
import javax.inject.Inject

abstract class AbstractFragment<VM>: Fragment() where VM: AbstractViewModel<ViewType> {

  @Inject lateinit var viewModel: VM
  lateinit var viewDataBinding: ViewDataBinding

  override fun onPause() {
    viewModel.onPause()
    super.onPause()
  }

  override fun onResume() {
    super.onResume()
    viewModel.onResume()
  }

  override fun onStart() {
    super.onStart()
    viewModel.onStart()
  }

  override fun onStop() {
    viewModel.onStop()
    super.onStop()
  }

  override fun onDestroy() {
    viewModel.onDestroy()
    super.onDestroy()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    viewModel.storeState(outState)
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
      grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    viewModel.requestPermissionsResult(requestCode, permissions, grantResults)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    viewModel.activityResult(requestCode, resultCode, data)
  }

  open fun showProgress() {
    throw RuntimeException("you need to implement this method in super type")
  }

  open fun hideProgress() {
    throw RuntimeException("you need to implement this method in super type")
  }

  open fun showError(error: String, action: String? = null,
      callback: View.OnClickListener? = null) {
    val view = view();
    if (view != null) {
      var snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
      if (action != null) {
        snackbar = snackbar.setAction(action) { v: View ->
          callback?.onClick(v)
        }
        snackbar.show()
      } else {
        snackbar.show()
      }
    }
  }

  fun getSupportFragmentManager(): FragmentManager = childFragmentManager

  fun finish() {
    throw RuntimeException("you can not finish this fragment")
  }

  fun getStringResource(stringRes: Int): String? = getString(stringRes)

  fun isAvailable(): Boolean = isAdded && activity != null

  fun view(): View? = view
}