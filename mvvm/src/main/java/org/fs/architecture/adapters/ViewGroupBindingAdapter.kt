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
package org.fs.architecture.adapters

import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup

class ViewGroupBindingAdapter private constructor() {

  companion object {

    @BindingAdapter(value = ["bindings:populate", "bindings:model", "bindings:variableId"])
    @JvmStatic fun viewGroupBindingAdapter(viewGroup: ViewGroup, @LayoutRes layoutRes: Int, viewModel: Any, variableId: Int) {
      if (layoutRes != 0) {
        // viewGroup.removeAllViews()
        val factory = LayoutInflater.from(viewGroup.context)
        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(factory, layoutRes, viewGroup, true)
        viewDataBinding.setVariable(variableId, viewModel)
        viewDataBinding.executePendingBindings()
      }
    }
  }
}