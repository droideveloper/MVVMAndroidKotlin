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
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import org.fs.architecture.R
import org.fs.architecture.common.AbstractPagerAdapter
import org.fs.architecture.common.AbstractStatePagerAdapter
import org.fs.architecture.listeners.OnPageScrollStateChanged
import org.fs.architecture.listeners.OnPageScrolled
import org.fs.architecture.listeners.OnPageSelected
import org.fs.architecture.model.PropertyInfo
import org.fs.architecture.util.Properties


class ViewPagerBindingAdapter private constructor() {


  companion object {

    @InverseBindingAdapter(attribute = "selectedPage", event = "selectedPageAttrChanged")
    @JvmStatic fun viewPagerBindSelectedPage(viewPager: ViewPager): Int = viewPager.currentItem

    @BindingAdapter(value = ["selectedPage"])
    @JvmStatic fun viewPagerBindSelectedPage(viewPager: ViewPager, selectedPage: Int) {
      viewPager.setCurrentItem(selectedPage, true)
    }

    @InverseBindingAdapter(attribute = "selectedItem", event = "selectedItemAttrChanged")
    @JvmStatic fun <T> viewPagerBindSelectedItem(viewPager: ViewPager): T? {
      val property = Properties.getPropertyInfo<T>(viewPager, R.id.viewPagerSelectedItem)
      return property?.value
    }

    @BindingAdapter(value = ["selectedItem"])
    @JvmStatic fun <T> viewPagerBindSelectedItem(viewPager: ViewPager, item: T?) {
      var property = Properties.getPropertyInfo<T>(viewPager, R.id.viewPagerSelectedItem)
      if (property != null) {
        if (property.value != item) {
          property = PropertyInfo(item)
        }
      } else {
        property = PropertyInfo(item)
      }
      Properties.setPropertyInfo(viewPager, property, R.id.viewPagerSelectedItem)
    }

    @BindingAdapter(value = ["pageTransformer"])
    @JvmStatic fun viewPagerBindPageTransformer(viewPager: ViewPager, pageTransformer: ViewPager.PageTransformer?) {
      viewPager.setPageTransformer(false, pageTransformer)
    }

    @BindingAdapter(value = ["itemSource"])
    @JvmStatic fun viewPagerBindItemSource(viewPager: ViewPager, itemSource: PagerAdapter) {
      viewPager.adapter = itemSource
    }

    @BindingAdapter(value = ["pageScrolled",
        "pageSelected",
        "pageScrollStateChanged",
        "selectedPageAttrChanged",
        "selectedItemAttrChanged"], requireAll = false)
    @JvmStatic fun viewPagerBindPageChangeListener(viewPager: ViewPager, pageScrolled: OnPageScrolled?, pageSelected: OnPageSelected?, pageScrollStateChanged: OnPageScrollStateChanged?, selectedPageAttr: InverseBindingListener?, selectedItemAttr: InverseBindingListener?) {
      var newPageChangeListener: ViewPager.OnPageChangeListener? = null
      if (pageSelected != null || pageScrolled != null || pageScrollStateChanged != null || selectedPageAttr != null || selectedItemAttr != null) {
        newPageChangeListener = object: ViewPager.OnPageChangeListener {

          override fun onPageScrollStateChanged(state: Int) {
            pageScrollStateChanged?.pageScrollStateChanged(state)
          }

          override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            pageScrolled?.pageScrolled(position, positionOffset, positionOffsetPixels)
          }

          override fun onPageSelected(position: Int) {
            pageSelected?.pageSelected(position)
            selectedPageAttr?.onChange()
            val adapter = viewPager.adapter

            if (adapter is AbstractPagerAdapter<*>) {
              val item = adapter.itemAt(position)
              val property = PropertyInfo(item)
              Properties.setPropertyInfo(viewPager, property, R.id.viewPagerSelectedItem)
              selectedItemAttr?.onChange()

            } else if (adapter is AbstractStatePagerAdapter<*>) {
              val item = adapter.itemAt(position)
              val property = PropertyInfo(item)
              Properties.setPropertyInfo(viewPager, property, R.id.viewPagerSelectedItem)
              selectedItemAttr?.onChange()
            }
          }
        }
      }
      val oldPageChangeListener = ListenerUtil.trackListener(viewPager, newPageChangeListener, R.id.pageChangeListener)
      if (oldPageChangeListener != null) {
        viewPager.removeOnPageChangeListener(oldPageChangeListener)
      }
      if (newPageChangeListener != null) {
        viewPager.addOnPageChangeListener(newPageChangeListener)
      }
    }
  }
}