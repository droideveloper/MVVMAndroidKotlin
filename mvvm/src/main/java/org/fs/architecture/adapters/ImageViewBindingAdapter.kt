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
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.fs.architecture.model.GlideTargetType

class ImageViewBindingAdapter private constructor() {

  companion object {

    @BindingAdapter(value = ["imageUrl", "placeholder", "error"], requireAll = false)
    @JvmStatic fun viewImageBindUrl(viewImage: ImageView, imageUrl: String?, placeholder: Drawable?, error: Drawable?) {
      if (imageUrl != null) {
        var request = Glide.with(viewImage.context)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        if (placeholder != null) {
          request = request.placeholder(placeholder)
        }
        if (error != null) {
          request = request.error(error)
        }
        request.dontAnimate()
          .into(viewImage)
      }
    }

    @BindingAdapter(value = ["imageUrl", "glideTarget"])
    @JvmStatic fun viewImageBindBitmapUrl(viewImage: ImageView, imageUrl: String?, glideTarget: GlideTargetType?) {
      if (imageUrl != null) {
        val request = Glide.with(viewImage.context)
            .load(imageUrl)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        if (glideTarget != null) {
          val target = glideTarget.targetType(viewImage)
          request.into(target)
        }
      }
    }
  }
}
