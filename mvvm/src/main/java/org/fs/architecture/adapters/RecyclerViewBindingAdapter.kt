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
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import org.fs.architecture.common.AbstractRecyclerViewAdapter

class RecyclerViewBindingAdapter private constructor() {

  companion object {

    @BindingAdapter("itemSource")
    @JvmStatic fun <T> recyclerViewBindItemSource(viewRecycler: RecyclerView, adapter: AbstractRecyclerViewAdapter<T>?) {
      viewRecycler.adapter = adapter
    }

    @BindingAdapter("layoutManager", "fixedSize")
    @JvmStatic fun recyclerViewBindLayoutManager(viewRecycler: RecyclerView, layoutManager: RecyclerView.LayoutManager?, fixedSize: Boolean = true) {
      viewRecycler.setHasFixedSize(fixedSize)
      viewRecycler.layoutManager = layoutManager
    }

    @BindingAdapter("itemTouchHelper")
    @JvmStatic fun recyclerViewBindItemTouchHelper(viewRecycler: RecyclerView, touchHelper: ItemTouchHelper) {
      touchHelper.attachToRecyclerView(viewRecycler)
    }

    @BindingAdapter("itemAnimator")
    @JvmStatic fun recyclerViewBindItemAnimator(viewRecycler: RecyclerView, itemAnimator: RecyclerView.ItemAnimator) {
      viewRecycler.itemAnimator = itemAnimator
    }

    @BindingAdapter("cacheSize")
    @JvmStatic fun recyclerViewBindCache(viewRecycler: RecyclerView, cacheSize: Int = 0) {
      if (cacheSize != 0) {
        viewRecycler.destroyDrawingCache()
        viewRecycler.isDrawingCacheEnabled = false
      } else {
        viewRecycler.isDrawingCacheEnabled = true
        viewRecycler.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        viewRecycler.setItemViewCacheSize(cacheSize)
      }
    }
  }
}