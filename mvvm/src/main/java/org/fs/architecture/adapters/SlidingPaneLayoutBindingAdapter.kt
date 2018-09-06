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
import android.support.v4.widget.SlidingPaneLayout
import android.view.View
import org.fs.architecture.listeners.OnLayoutOpenStateChanged
import org.fs.architecture.listeners.OnLayoutSlided

sealed class SlidingPaneLayoutBindingAdapter {

  companion object {

    @BindingAdapter(value = ["open"])
    @JvmStatic fun slidingPaneLayoutBindOpen(slidingPaneLayout: SlidingPaneLayout, open: Boolean) {
      if (slidingPaneLayout.isOpen != open) {
        if (open) {
          slidingPaneLayout.openPane()
        } else {
          slidingPaneLayout.closePane()
        }
      }
    }

    @InverseBindingAdapter(attribute = "open", event = "openAttrChanged")
    @JvmStatic fun slidingPaneLayoutBindOpen(
        slidingPaneLayout: SlidingPaneLayout): Boolean = slidingPaneLayout.isOpen

    @BindingAdapter(
        value = ["slided", "openState", "openAttrChanged"],
        requireAll = false)
    @JvmStatic fun slidingPaneLayoutBindSlidedListener(slidingPaneLayout: SlidingPaneLayout,
        slided: OnLayoutSlided?, openStateChanged: OnLayoutOpenStateChanged?,
        openAttr: InverseBindingListener?) {
      var slideListener: SlidingPaneLayout.PanelSlideListener? = null
      if (slided != null || openStateChanged != null || openAttr != null) {
        slideListener = object : SlidingPaneLayout.PanelSlideListener {
          override fun onPanelSlide(panel: View, slideOffset: Float) {
            slided?.layoutSlided(slidingPaneLayout, slideOffset)
          }

          override fun onPanelOpened(panel: View) {
            openStateChanged?.layoutOpenedOrClosed(true)
            openAttr?.onChange()
          }

          override fun onPanelClosed(panel: View) {
            openStateChanged?.layoutOpenedOrClosed(false)
            openAttr?.onChange()
          }
        }
      }
      slidingPaneLayout.setPanelSlideListener(slideListener)
    }
  }
}