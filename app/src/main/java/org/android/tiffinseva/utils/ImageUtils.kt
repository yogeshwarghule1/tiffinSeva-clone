package org.android.tiffinseva.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.squareup.picasso.Picasso

class ImageUtils private constructor() {

    fun loadBgImage(path: String, view: ImageView, placeHolder: Drawable?, errorImage: Drawable? = null) {
        Picasso.get().load(path).placeholder(placeHolder!!).into(view)
    }


    fun loadBgImage(imageView: ImageView, imageUrl: String?) {
        Picasso.get().load(imageUrl).into(imageView)
    }

    companion object {
        private var mInstance: ImageUtils? = null
        val instance: ImageUtils?
            get() {
                if (mInstance == null) {
                    synchronized(ImageUtils::class.java) { mInstance = ImageUtils() }
                }
                return mInstance
            }
    }
}