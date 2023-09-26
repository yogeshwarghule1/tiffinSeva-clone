package org.android.tiffinseva.base

@Deprecated("Use typeSafe DataEvent")
data class ViewEvent(var event: Int, var data: Any)

data class DataEvent<T>(var event: Int, var data: T)

const val ERROR_ID = 1
const val SUCCESS_ID = 2
const val SHOW_PROGRESS_BAR = 3
const val HIDE_PROGRESS_BAR = 4