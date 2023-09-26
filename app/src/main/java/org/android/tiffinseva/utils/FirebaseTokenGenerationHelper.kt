package org.android.tiffinseva.utils
import com.google.firebase.iid.FirebaseInstanceId
import timber.log.Timber

open class FirebaseTokenGenerationHelper(private val firebaseToken: IFirebaseToken){
    open fun getFirebaseInstance() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.w("getInstanceId failed" + task.exception)
                }
                val token = task.result?.token
                firebaseToken!!.onTokenChange(token)
                Timber.d("TOKEN %s", token)
            }
    }

    interface IFirebaseToken {
        fun onTokenChange(token: String?)
    }
}

