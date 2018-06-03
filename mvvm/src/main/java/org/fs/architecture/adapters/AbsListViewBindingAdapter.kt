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
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView
import org.fs.architecture.common.AbstractListAdapter

class AbsListViewBindingAdapter private constructor() {

  companion object {

    @BindingAdapter("bindings:drawingCacheEnabled")
    @JvmStatic fun absListViewCacheSize(absListView: AbsListView, enabled: Boolean = false) {
      if (!enabled) {
        absListView.destroyDrawingCache()
        absListView.isDrawingCacheEnabled = enabled
      } else {
        absListView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        absListView.isDrawingCacheEnabled = enabled
      }
    }

    @BindingAdapter("bindings:itemSource")
    @JvmStatic fun <T> absListViewBindAdapter(absListView: AbsListView, adapter: AbstractListAdapter<T>?) {
      absListView.adapter = adapter
    }

    @BindingAdapter("bindings:scrollListener")
    @JvmStatic fun absListViewBindScrollListener(absListView: AbsListView, listener: AbsListView.OnScrollListener) {
      absListView.setOnScrollListener(listener)
    }

    @BindingAdapter("bindings:itemClickListener")
    @JvmStatic fun absListViewClickListener(absListView: AbsListView, listener: AdapterView.OnItemClickListener) {
      absListView.onItemClickListener = listener
    }

    @BindingAdapter("bindings:itemSelectedListener")
    @JvmStatic fun absListViewSelectedListener(absListView: AbsListView, listener: AdapterView.OnItemSelectedListener) {
      absListView.onItemSelectedListener = listener
    }

    @BindingAdapter("bindings:itemLongClickListener")
    @JvmStatic fun absListViewLongClickListener(absListView: AbsListView, listener: AdapterView.OnItemLongClickListener) {
      absListView.onItemLongClickListener = listener
    }
  }
}