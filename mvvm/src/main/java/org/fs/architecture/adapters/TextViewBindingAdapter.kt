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
import android.databinding.InverseBindingListener
import android.databinding.adapters.ListenerUtil
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import org.fs.architecture.R
import org.fs.architecture.listeners.OnAfterChanged
import org.fs.architecture.listeners.OnBeforeChanged
import org.fs.architecture.listeners.OnSoftKeyboardAction
import org.fs.architecture.model.ConverterType
import java.util.*

sealed class TextViewBindingAdapter {

  companion object {

    @BindingAdapter(value = ["imeOptions"])
    @JvmStatic fun textViewBindImeOptions(viewText: TextView, imeOptions: Int) {
      viewText.imeOptions = imeOptions
    }

    @BindingAdapter(value = ["softKeyboardAction"])
    @JvmStatic fun textViewBindSoftKeyboardAction(viewText: TextView, keyboardAction: OnSoftKeyboardAction?) {
      if (keyboardAction == null) {
        viewText.setOnEditorActionListener(null)
      } else {
        viewText.setOnEditorActionListener { _, actionId, _ -> keyboardAction.editorAction(actionId) }
      }
    }

    @BindingAdapter(value = ["value", "converter"], requireAll = false)
    @JvmStatic fun <T, S> textViewBindText(viewText: TextView, value: T, converter: ConverterType<T, S>?) where S: CharSequence {
      if (converter == null) {
        if (value is CharSequence) {
          viewText.text = value
        }
      } else {
        val text = converter.convert(value, Locale.getDefault())
        viewText.text =  text
      }
    }

    @BindingAdapter(value = ["beforeChanged", "afterChanged", "textAttrChanged"], requireAll = false)
    @JvmStatic fun textViewBindTextWatcher(viewText: TextView, beforeChanged: OnBeforeChanged?, afterChanged: OnAfterChanged?, textAttrChanged: InverseBindingListener?) {
      var newTextWatcher: TextWatcher? = null
      if (textAttrChanged != null || beforeChanged != null || afterChanged != null) {
        newTextWatcher = object: TextWatcher {

          override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { textAttrChanged?.onChange() }
          override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { beforeChanged?.beforeChanged(s, start, count, after) }
          override fun afterTextChanged(s: Editable?) { afterChanged?.afterChanged(s) }
        }
      }
      val oldTextWatcher = ListenerUtil.trackListener(viewText, newTextWatcher, R.id.textWatcher)
      if (oldTextWatcher != null) {
        viewText.removeTextChangedListener(oldTextWatcher)
      }
      if (newTextWatcher != null) {
        viewText.addTextChangedListener(newTextWatcher)
      }
    }
  }
}