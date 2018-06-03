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
import android.databinding.adapters.ListenerUtil
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import org.fs.architecture.R
import org.fs.architecture.listeners.SimpleTextWatcher
import org.fs.architecture.model.ValidatorType
import java.util.*

class TextInputLayoutBindingAdapter private constructor() {

  companion object {

    @BindingAdapter(value = ["hint"])
    @JvmStatic fun <S> textInputLayoutBindHint(textInputLayout: TextInputLayout, hint: S) where S: CharSequence {
      textInputLayout.hint = hint
      textInputLayout.isHintEnabled = true
    }

    @BindingAdapter(value = ["error", "validator"])
    @JvmStatic fun <S> textInputLayoutBindError(textInputLayout: TextInputLayout, error: S?, validator: ValidatorType<String>?) where S: CharSequence {
      val textView = textView(textInputLayout)
      if (textView != null) {
        var newListener: TextWatcher? = null
        if (error != null && validator != null) {
          newListener = object: SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
              val validation = validator.validate(s.toString(), Locale.getDefault())
              if (validation.valid) {
                textInputLayout.error = ""
                textInputLayout.isErrorEnabled = false
              } else {
                textInputLayout.error = error
                textInputLayout.isErrorEnabled = true
              }
            }
          }
        }
        val oldListener = ListenerUtil.trackListener(textView, newListener, R.id.textWatcher)
        if (oldListener != null) {
          textView.removeTextChangedListener(oldListener)
        }
        if (newListener != null) {
          textView.addTextChangedListener(newListener)
        }
      }
    }

    @JvmStatic private fun textView(viewGroup: ViewGroup): TextView? {
      (0..viewGroup.childCount)
        .map { viewGroup.getChildAt(it) }
        .forEach {
          if (it is EditText) {
            return it
          } else if (it is ViewGroup) {
            return textView(it)
          }
        }
      return null
    }
  }
}