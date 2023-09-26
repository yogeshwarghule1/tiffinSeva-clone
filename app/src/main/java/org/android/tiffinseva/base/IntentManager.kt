package org.android.tiffinseva.base

import android.content.Context
import android.content.Intent
import org.android.tiffinseva.loginflow.LoginActivity

class IntentManager {

    companion object {
        fun startLoginActivity(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}