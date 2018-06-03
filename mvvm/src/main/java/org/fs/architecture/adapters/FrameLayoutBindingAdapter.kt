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
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.widget.FrameLayout
import org.fs.architecture.R

class FrameLayoutBindingAdapter private constructor() {

  companion object {
    /*
    @BindingAdapter(value = *arrayOf("bindings:fragment", "bindings:fragmentManager", "bindings:enterAnim", "bindings:exitAnim", "bindings:reverse"))
    @JvmStatic fun frameLayoutBindFragment(frameLayout: FrameLayout, fragment: Fragment?, fragmentManager: FragmentManager?,
        @AnimRes enter: Int = android.R.anim.fade_in, @AnimRes exit: Int = android.R.anim.fade_out, reverse: Boolean = false) {
      if (frameLayout.id == -1) {
        frameLayout.id = R.id.viewContentLayout
      }
      if (fragment != null && fragmentManager != null) {
        var trans = fragmentManager.beginTransaction()
        if (reverse) {
          trans.setCustomAnimations(exit, enter)
        } else {
          trans.setCustomAnimations(enter, exit)
        }
        trans.replace(frameLayout.id, fragment)
        trans.commit()
      }
    }
    */

    @BindingAdapter(value = ["fragment", "fragmentManager"])
    @JvmStatic fun frameLayoutBindFragment(frameLayout: FrameLayout, fragment: Fragment?, fragmentManager: FragmentManager?) {
      if (frameLayout.id == -1) {
        frameLayout.id = R.id.viewContentLayout
      }
      if (fragment != null && fragmentManager != null) {
        val trans = fragmentManager.beginTransaction()
            .replace(frameLayout.id, fragment)
        trans.commit()
      }
    }
  }
}