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

import android.databinding.DataBindingUtil
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

abstract class AbstractListAdapter<T>(protected val dataSet: ObservableList<T>): BaseAdapter() {

  private var factory: LayoutInflater? = null

  private val dataObserver = object: ObservableList.OnListChangedCallback<ObservableList<T>>() {
    override fun onItemRangeRemoved(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) = notifyDataSetChanged()
    override fun onItemRangeInserted(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) = notifyDataSetChanged()
    override fun onItemRangeChanged(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) = notifyDataSetChanged()
    override fun onChanged(sender: ObservableList<T>?) = notifyDataSetChanged()
    override fun onItemRangeMoved(sender: ObservableList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) = notifyDataSetChanged()
  }

  fun registerDataObserver() = dataSet.addOnListChangedCallback(dataObserver)
  fun unregisterDataObserver() = dataSet.removeOnListChangedCallback(dataObserver)

  @Suppress("UNCHECKED_CAST")
  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    val factory = factory(parent)
    val viewType = getItemViewType(position)
    if (convertView == null) {
      if (factory != null) {
        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(factory, layoutRes(viewType), parent, false)
        val viewHolder = createViewHolder(viewDataBinding, viewType)
        val item = itemAt(position)
        bindViewHolder(viewHolder, item, position)
        val view = viewDataBinding.root
        view.tag = viewHolder
        return view
      } else {
        throw RuntimeException("factory for view creation is not available")
      }
    } else {
      val viewHolder = convertView.tag as? AbstractViewHolder<T>
      if (viewHolder != null) {
        val item = itemAt(position)
        bindViewHolder(viewHolder, item, position)
      }
      return convertView
    }
  }

  override fun getCount(): Int = dataSet.size

  fun bindViewHolder(viewHolder: AbstractViewHolder<T>, item: T, position: Int) {
    viewHolder.position = position
    viewHolder.setVariable(bindingRes(), item)
  }

  abstract fun createViewHolder(viewDataBinding: ViewDataBinding, viewType: Int): AbstractViewHolder<T>
  abstract fun layoutRes(viewType: Int): Int
  abstract fun bindingRes(): Int

  fun itemAt(position: Int): T = dataSet[position]

  fun factory(parent: ViewGroup?): LayoutInflater? {
    if (factory == null) {
      if (parent != null) {
        factory = LayoutInflater.from(parent.context)
      }
    }
    return factory
  }
}