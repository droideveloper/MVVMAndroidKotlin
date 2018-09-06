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
import android.view.ViewGroup
import org.fs.architecture.util.toFactory

abstract class AbstractRecyclerViewAdapter<T, VH>(private val dataSet: ObservableList<T>)
  : RecyclerView.Adapter<VH>() where VH: AbstractRecyclerViewHolder<T>  {

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
    dataSet.removeOnListChangedCallback(dataSetObserver)
    super.onDetachedFromRecyclerView(recyclerView)
  }

  override fun onBindViewHolder(viewHolder: VH, position: Int) {
    viewHolder.setVariable(bindingRes(), dataSet[position])
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
    val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(parent.toFactory(), layoutRes(viewType), parent, false)
    return onCreateViewHolder(viewDataBinding, viewType)
  }

  abstract fun onCreateViewHolder(viewDataBinding: ViewDataBinding, viewType: Int): VH
  abstract fun layoutRes(viewType: Int): Int
  abstract fun bindingRes(): Int
  override fun getItemCount(): Int = dataSet.size
}