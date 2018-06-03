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
import android.databinding.adapters.ListenerUtil
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.View
import org.fs.architecture.R
import org.fs.architecture.listeners.OnLayoutOpenStateChanged
import org.fs.architecture.listeners.OnLayoutSlided
import org.fs.architecture.listeners.OnLayoutStateChanged

class DrawerLayoutBindingAdapter private constructor() {

  companion object {

    @BindingAdapter(value = ["open"])
    @JvmStatic
    fun drawerLayoutBindOpen(drawerLayout: DrawerLayout, open: Boolean) {
      val currentState = drawerOpen(drawerLayout)
      if (currentState) {
        if (!open) {
          closeDrawer(drawerLayout)
        }
      } else {
        if (open) {
          openDrawer(drawerLayout)
        }
      }
    }

    @InverseBindingAdapter(attribute = "open", event = "openAttrChanged")
    @JvmStatic
    fun drawerLayoutBindOpen(drawerLayout: DrawerLayout): Boolean = drawerOpen(drawerLayout)

    @BindingAdapter(
        value = ["slided", "openOrClose", "stateChanged", "openAttrChanged"], requireAll = false)
    @JvmStatic
    fun drawableLayoutBindListener(drawerLayout: DrawerLayout, slided: OnLayoutSlided?,
        openStateChanged: OnLayoutOpenStateChanged?, stateChanged: OnLayoutStateChanged?,
        openAttrListener: InverseBindingListener?) {
      var newListener: DrawerLayout.DrawerListener? = null
      if (slided != null || openStateChanged != null || stateChanged != null || openAttrListener != null) {
        newListener = object : DrawerLayout.DrawerListener {
          override fun onDrawerOpened(drawerView: View) {
            openStateChanged?.layoutOpenedOrClosed(true)
            openAttrListener?.onChange()
          }

          override fun onDrawerClosed(drawerView: View) {
            openStateChanged?.layoutOpenedOrClosed(false)
            openAttrListener?.onChange()
          }

          override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            slided?.layoutSlided(drawerLayout, slideOffset)
          }

          override fun onDrawerStateChanged(newState: Int) {
            stateChanged?.stateChanged(newState)
          }
        }
      }
      val oldListener = ListenerUtil.trackListener(drawerLayout, newListener, R.id.drawerListener)
      if (oldListener != null) {
        drawerLayout.removeDrawerListener(oldListener)
      }
      if (newListener != null) {
        drawerLayout.addDrawerListener(newListener)
      }
    }

    @JvmStatic private fun closeDrawer(drawerLayout: DrawerLayout) {
      try {
        drawerLayout.closeDrawer(Gravity.START)
      } catch (error: Exception) {
      }
      try {
        drawerLayout.closeDrawer(Gravity.END)
      } catch (error: Exception) {
      }
    }

    @JvmStatic private fun openDrawer(drawerLayout: DrawerLayout) {
      try {
        drawerLayout.openDrawer(Gravity.START)
      } catch (error: Exception) {
      }
      try {
        drawerLayout.openDrawer(Gravity.END)
      } catch (error: Exception) {
      }
    }

    @JvmStatic private fun drawerOpen(
        drawerLayout: DrawerLayout): Boolean = drawerLayout.isDrawerOpen(
        Gravity.START) || drawerLayout.isDrawerOpen(Gravity.END)
  }
}