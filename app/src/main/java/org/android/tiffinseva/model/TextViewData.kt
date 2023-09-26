package org.android.tiffinseva.model

data class TextViewData(var textData: String, var textHint: String, var drawableId: Int? = null, var handler: TTSTextViewClickListener? = null)

interface TTSTextViewClickListener {
    fun onTextViewClick(textViewData: TextViewData)
}