package org.android.tiffinseva.utils

interface ClickHandler<Any> {
    fun onViewClick(id: Int, data: Any)
}

interface BaseClickHandler {
    fun onViewClick()
}