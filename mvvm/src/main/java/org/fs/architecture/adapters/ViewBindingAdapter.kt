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

import android.animation.Animator
import android.animation.AnimatorInflater
import android.databinding.BindingAdapter
import android.databinding.adapters.ListenerUtil
import android.support.annotation.AnimRes
import android.support.annotation.AnimatorRes
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.widget.ProgressBar
import org.fs.architecture.R
import org.fs.architecture.common.CommandType
import org.fs.architecture.common.RelayCommandType
import org.fs.architecture.listeners.OnLayoutSlided
import org.fs.architecture.listeners.OnLayoutStateChanged


sealed class ViewBindingAdapter {

  companion object {

    @BindingAdapter(value = ["anim", "interpolator", "animListener"], requireAll = false)
    @JvmStatic fun viewBindAnimation(view: View, @AnimRes animRes: Int, interpolator: Interpolator?, callback: Animation.AnimationListener?) {
      val animation = AnimationUtils.loadAnimation(view.context, animRes)
      if (animation != null) {
        if (interpolator != null) {
          animation.interpolator = interpolator
        }
        if (callback != null) {
          animation.setAnimationListener(callback)
        }
        view.animation = animation
        animation.start()
      }
    }

    @BindingAdapter(value = ["animator", "interpolator", "animatorListener"], requireAll = false)
    @JvmStatic fun viewBindAnimator(view: View, @AnimatorRes animatorRes: Int, interpolator: Interpolator?, callback: Animator.AnimatorListener?) {
      val animator = AnimatorInflater.loadAnimator(view.context, animatorRes)
      if (animator != null) {
        if (interpolator != null) {
          animator.interpolator = interpolator
        }
        if (callback != null) {
          animator.addListener(callback)
        }
        animator.setTarget(view)
        animator.start()
      }
    }

    @BindingAdapter(value = ["notifyText"])
    @JvmStatic fun <S> viewBindSnackbar(view: View, text: S) where S: CharSequence {
      if (text.isNotEmpty()) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .show()
      }
    }

    @BindingAdapter(value = ["notifyText", "actionText", "relayCommand"])
    @JvmStatic fun <S> viewBindSnackbar(view: View, text: S, action: S, command: RelayCommandType) where S: CharSequence {
      if (text.isNotEmpty()) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction(action, { _ -> command.execute(null) })
      }
    }

    @BindingAdapter(value = ["command", "commandParameter"], requireAll = false)
    @JvmStatic fun <T> viewBindCommand(view: View, command: CommandType<T>?, parameter: T?) {
      if (command != null) {
        val oldListener = ListenerUtil.getListener<View.OnClickListener>(view, R.id.clickListener)
        val newListener = View.OnClickListener { _ ->
          if (command.canExecute(parameter)) {
            command.execute(parameter)
          }
          oldListener?.onClick(view)
        }
        view.setOnClickListener(newListener)
      }
    }

    @BindingAdapter(value = ["bottomSheetState"])
    @JvmStatic fun viewBindBottomSheetBehaviorState(view: View, state: Int) {
      if (state != 0) {
        val lp = view.layoutParams as? CoordinatorLayout.LayoutParams
        if (lp != null) {
          val behavior = lp.behavior as? BottomSheetBehavior
          if (behavior != null) {
            behavior.state = state
          }
        }
      }
    }

    @BindingAdapter(value = ["touchCallback"])
    @JvmStatic fun viewBindTouchListener(view: View, callback: View.OnTouchListener?) = view.setOnTouchListener(callback)

    @BindingAdapter(value = ["hideable", "peekHeight", "stateChange", "slided"], requireAll = false)
    @JvmStatic fun viewBindBottomSheetBehavior(view: View, hideable: Boolean?, peekHeight: Int?, stateChange: OnLayoutStateChanged?, slided: OnLayoutSlided?) {
      if (hideable != null || peekHeight != null || stateChange != null || slided != null) {
        val behavior = BottomSheetBehavior.from(view)
        if (hideable != null) {
          behavior.isHideable = hideable
        }
        if (peekHeight != null) {
          behavior.peekHeight = peekHeight
        }
        if (stateChange != null || slided != null) {
          behavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
              slided?.layoutSlided(view, slideOffset)
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
              stateChange?.stateChanged(newState)
            }
          })
        }
      }
    }

    @BindingAdapter(value = ["visibility"])
    @JvmStatic fun viewBindVisibility(view: View, visibility: Boolean) {
      view.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    @BindingAdapter(value = ["indeterminate"])
    @JvmStatic fun progressBarBindIndeterminate(progressBar: ProgressBar, indeterminate: Boolean) {
      progressBar.isIndeterminate = indeterminate
    }

    @BindingAdapter("android:enabled")
    @JvmStatic fun viewBindEnabled(view: View, enabled: Boolean) {
      view.isEnabled = enabled
    }
  }
}