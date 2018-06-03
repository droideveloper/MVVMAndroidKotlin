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
import android.databinding.InverseBindingAdapter
import android.databinding.InverseBindingListener
import android.support.v4.widget.SwipeRefreshLayout
import org.fs.architecture.listeners.OnRefreshed

class SwipeRefreshLayoutBindingAdapter private constructor() {

  companion object {

    @InverseBindingAdapter(attribute = "refreshing", event = "refreshingAttrChanged")
    @JvmStatic fun swipeRefreshLayoutRefreshing(viewSwipe: SwipeRefreshLayout): Boolean {
      return viewSwipe.isRefreshing
    }

    @BindingAdapter(value = ["refreshing"])
    @JvmStatic fun swipeRefreshLayoutBindRehreshing(viewSwipe: SwipeRefreshLayout, refreshing: Boolean) {
      viewSwipe.isRefreshing = refreshing
    }

    @BindingAdapter(value = ["callback", "refreshingAttrChanged"])
    @JvmStatic fun swipeRefreshLayoutBindCallback(viewSwipe: SwipeRefreshLayout, refreshed: OnRefreshed?, refreshingAttr: InverseBindingListener?) {
      var newListener: SwipeRefreshLayout.OnRefreshListener? = null
      if (refreshed != null || refreshingAttr != null) {
        newListener = SwipeRefreshLayout.OnRefreshListener {
          refreshed?.refreshed()
          refreshingAttr?.onChange()
        }
      }
      viewSwipe.setOnRefreshListener(newListener)
    }
  }
}