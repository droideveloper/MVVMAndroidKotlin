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
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class AbstractRecyclerViewAdapter<T>(private val dataSet: ObservableList<T>): RecyclerView.Adapter<AbstractRecyclerViewHolder<T>>() {

  private var factory: LayoutInflater? = null

  private val dataSetObserver = object: ObservableList.OnListChangedCallback<ObservableList<T>>() {
    override fun onChanged(sender: ObservableList<T>?) = notifyDataSetChanged()
    override fun onItemRangeChanged(sender: ObservableList<T>?, start: Int, count: Int) = notifyItemRangeChanged(start, count)
    override fun onItemRangeInserted(sender: ObservableList<T>?, start: Int, count: Int) = notifyItemRangeInserted(start, count)
    override fun onItemRangeRemoved(sender: ObservableList<T>?,start: Int, count: Int) = notifyItemRangeRemoved(start, count)
    override fun onItemRangeMoved(sender: ObservableList<T>?, from: Int, to: Int, count: Int)  {
      for (i in 0..count) {
        notifyItemMoved(i + from, i + to)
      }
    }
  }

  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    super.onAttachedToRecyclerView(recyclerView)
    dataSet.addOnListChangedCallback(dataSetObserver)
  }

  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    super.onDetachedFromRecyclerView(recyclerView)
    dataSet.removeOnListChangedCallback(dataSetObserver)
  }

  override fun onBindViewHolder(viewHolder: AbstractRecyclerViewHolder<T>, position: Int) {
    val item = itemAt(position)
    viewHolder.setVariable(bindingRes(), item)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractRecyclerViewHolder<T> {
    val factory = factory(parent)
    if (factory != null) {
      val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(factory, layoutRes(viewType), parent, false)
      return recreateViewHolder(viewDataBinding, viewType)
    } else {
      throw RuntimeException("you can not have instance of layout inflater factory")
    }
  }

  abstract fun recreateViewHolder(viewDataBinding: ViewDataBinding, viewType: Int): AbstractRecyclerViewHolder<T>
  abstract fun layoutRes(viewType: Int): Int
  abstract fun bindingRes(): Int

  override fun getItemCount(): Int = dataSet.size
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