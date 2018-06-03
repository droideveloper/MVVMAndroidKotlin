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
package org.fs.architecture.net.converters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonWriter
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Converter
import java.io.OutputStreamWriter
import java.nio.charset.Charset

class GsonRequestBodyConverter<T>(private val typeAdapter: TypeAdapter<T>): Converter<T, RequestBody> {

  private val mimeType: MediaType? = MediaType.parse("application/json; charset=UTF-8")
  private val charset: Charset = Charset.forName("UTF-8")

  override fun convert(value: T): RequestBody {
    val buffer = Buffer()
    val writer = OutputStreamWriter(buffer.outputStream(), charset)
    val jw = JsonWriter(writer)
    typeAdapter.write(jw, value)
    jw.close()
    return RequestBody.create(mimeType, buffer.readByteString())
  }
}