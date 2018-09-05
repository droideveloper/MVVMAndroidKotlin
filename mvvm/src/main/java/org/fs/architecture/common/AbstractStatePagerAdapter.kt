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
package org.fs.architecture.common

import android.databinding.ObservableList
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

abstract class AbstractStatePagerAdapter<T>(fragmentManager: FragmentManager, protected val dataSet: ObservableList<T>): FragmentStatePagerAdapter(fragmentManager) {

  private val dataObserver = object: ObservableList.OnListChangedCallback<ObservableList<T>>() {
    override fun onChanged(sender: ObservableList<T>?) = notifyDataSetChanged()
    override fun onItemRangeChanged(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) = notifyDataSetChanged()
    override fun onItemRangeInserted(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) = notifyDataSetChanged()
    override fun onItemRangeMoved(sender: ObservableList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) = notifyDataSetChanged()
    override fun onItemRangeRemoved(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) = notifyDataSetChanged()
  }

  override fun getItem(position: Int): Fragment = createFragment(viewTypeAt(position), dataSet[position])

  abstract fun viewTypeAt(position: Int): Int
  abstract fun createFragment(viewType: Int, item: T): Fragment

  open fun register() = dataSet.addOnListChangedCallback(dataObserver)
  open fun unregister() = dataSet.removeOnListChangedCallback(dataObserver)

  override fun getCount(): Int = dataSet.size
}