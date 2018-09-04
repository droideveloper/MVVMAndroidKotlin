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
import android.support.design.widget.NavigationView
import org.fs.architecture.listeners.OnNavigationSelected

sealed class NavigationViewBindingAdapter {

  companion object {
    private val NO_ID = -1

    @BindingAdapter(value = ["selectedItem"])
    @JvmStatic fun viewNavigationBindSelectedItem(viewNavigation: NavigationView, menuItemRes: Int = NO_ID) {
      if (menuItemRes != NO_ID) {
        val menu = viewNavigation.menu
        (0..menu.size())
            .map { menu.getItem(it) }
            .filter { it != null && it.itemId == menuItemRes }
            .forEach { it.isChecked = true }
      }
    }

    @InverseBindingAdapter(attribute = "selectedItem", event = "selectedItemAttrChanged")
    @JvmStatic fun viewNavigationBindSelectedItem(viewNavigation: NavigationView): Int {
      val menu = viewNavigation.menu
      (0..menu.size())
          .map { menu.getItem(it) }
          .filter { it != null && it.isChecked }
          .forEach { return it.itemId }
      return NO_ID
    }

    @BindingAdapter(value = ["onNavigationSelected", "selectedItemAttrChanged"], requireAll = false)
    @JvmStatic fun viewNavigationBindOnNavigationSelected(viewNavigation: NavigationView, navigationSelected: OnNavigationSelected?, selectedItemAttr: InverseBindingListener?) {
      if (navigationSelected != null || selectedItemAttr != null) {
        viewNavigation.setNavigationItemSelectedListener { item ->
          navigationSelected?.navigationSelected(item)
          item.isChecked = true
          selectedItemAttr?.onChange()
          true
        }
      } else {
        viewNavigation.setNavigationItemSelectedListener(null)
      }
    }
  }
}