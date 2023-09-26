package org.android.tiffinseva.model
import android.text.InputType
import androidx.databinding.ObservableField

data class EditTextData(var editTextData: ObservableField<String>, var textHint: String,
                        var errorText: ObservableField<String>? = null,
                        var maxLength: ObservableField<Int> = ObservableField(200),
                        var inputType: ObservableField<Int> = ObservableField(InputType.TYPE_CLASS_TEXT),
                        var handler: IEditTextChangeListener? = null)

interface IEditTextChangeListener {
    fun onTextChange(s: CharSequence,start: Int,before : Int, count :Int)
}
