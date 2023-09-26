package org.android.tiffinseva.utils

import android.content.res.AssetManager
import org.android.tiffinseva.TheTiffinSevaApp

interface IResourceProvider {
    fun getAssets(): AssetManager?
    fun getString(id: Int): String
}

object AppResourceProvider : IResourceProvider {
    override fun getAssets(): AssetManager {
        return TheTiffinSevaApp.getApplicationInstance().assets
    }

    override fun getString(id: Int): String {
        return TheTiffinSevaApp.getApplicationInstance().getString(id)
    }
}