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
import android.graphics.drawable.Drawable
import android.support.annotation.MenuRes
import android.support.v7.widget.Toolbar
import org.fs.architecture.common.CommandType
import org.fs.architecture.listeners.OnNavigated

class ToolbarBindingAdapter private constructor() {

  companion object {

    @BindingAdapter(value = ["bindings:icon"])
    @JvmStatic fun toolbarBindIcon(viewToolbar: Toolbar, drawable: Drawable) {
      viewToolbar.navigationIcon = drawable
    }

    @BindingAdapter(value = ["bindings:title"])
    @JvmStatic fun <S> toolbarBindTitle(viewToolbar: Toolbar, title: S) where S: CharSequence {
      viewToolbar.title = title
    }

    @BindingAdapter(value = ["bindings:subTitle"])
    @JvmStatic fun <S> toolbarBindSubTitle(viewToolbar: Toolbar, subTitle: S) where S: CharSequence {
      viewToolbar.subtitle = subTitle
    }

    @BindingAdapter(value = ["bindings:menu", "bindings:menuListener"], requireAll = false)
    @JvmStatic fun toolbarBindMenu(viewToolbar: Toolbar, @MenuRes menuRes: Int, callback: Toolbar.OnMenuItemClickListener?) {
      viewToolbar.inflateMenu(menuRes)
      if (callback != null) {
        viewToolbar.setOnMenuItemClickListener(callback)
      }
    }

    @BindingAdapter(value = ["bindings:onNavigated", "bindings:navigationCommand", "bindings:navigationCommandParameter"], requireAll = false)
    @JvmStatic fun <T> toolbarBindNavigationListener(viewToolbar: Toolbar, callback: OnNavigated?, command: CommandType<T>? , commandParameter: T?) {
      if (callback == null && command == null && commandParameter == null) {
        viewToolbar.setNavigationOnClickListener(null)
      } else {
        if (callback != null) {
          viewToolbar.setNavigationOnClickListener { _ ->
            callback.navigate(viewToolbar)
            if (command != null) {
              if (command.canExecute(commandParameter)) {
                command.execute(commandParameter)
              }
            }
          }
        }
      }
    }
  }
}