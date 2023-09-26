package org.android.tiffinseva
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.android.tiffinseva.base.ViewEvent
import org.android.tiffinseva.homeflow.HomeActivity
import org.android.tiffinseva.loginflow.LoginActivity
import org.android.tiffinseva.loginflow.loginfragment.LoginRequestTO
import org.android.tiffinseva.loginflow.loginfragment.LoginResponseTO
import org.android.tiffinseva.loginflow.loginfragment.LoginViewModel
import org.android.tiffinseva.loginflow.signupfragment.UserRespTO
import org.android.tiffinseva.networking.TTSError
import org.android.tiffinseva.networking.repository.ResultCallBack
import org.android.tiffinseva.pushnotif.FirebaseRepo
import org.android.tiffinseva.pushnotif.FirebaseTokenRqTO
import org.android.tiffinseva.utils.AppSharedPrefRepository
import timber.log.Timber

class PushNotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("From: ${remoteMessage.from}")
        remoteMessage.data.isNotEmpty().let {
            Timber.d("Message data payload: " + remoteMessage.data)
            handleNow()
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Timber.d("Message Notification Body: ${it.body}")
        }

         sendNotification(remoteMessage.notification)
    }
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Timber.d("Message Notification Body:$token")
        sendRegistrationToServer(token)
    }


    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Timber.d("Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        Timber.d("sendRegistrationTokenToServer($token)")
        AppSharedPrefRepository.getInstance().setString(AppConstants.FIREBASE_TOKEN, token!!)
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageBody: RemoteMessage.Notification?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, messageBody!!.channelId!!)
            .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody!!.body))
            .setSmallIcon(R.mipmap.new_app_logo)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.new_app_logo))
            .setContentTitle(messageBody!!.title)
            .setContentText(messageBody!!.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(messageBody.channelId!! == CHANNEL_ID_GEN) {
                notificationManager.createNotificationChannel(generalNotificationChannel())
            } else {
                notificationManager.createNotificationChannel(tiffinStatusNotificationChannel())
            }
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    /***
     * Method to generate general notification for app
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun generalNotificationChannel(): NotificationChannel {
        val generalMessageChannel = NotificationChannel(
            CHANNEL_ID_GEN, CHANNEL_NAME_GEN,
            NotificationManager.IMPORTANCE_DEFAULT)
        generalMessageChannel.lightColor = Color.GREEN
        generalMessageChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        return  generalMessageChannel
    }

    /***
     * Method to generate tiffin status notification channel
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun tiffinStatusNotificationChannel(): NotificationChannel {
        val statusNotificationChannel = NotificationChannel(
            CHANNEL_ID_STAT, CHANNEL_UPDATE_STAT,
            NotificationManager.IMPORTANCE_HIGH)
        statusNotificationChannel.lightColor = Color.YELLOW
        statusNotificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        return  statusNotificationChannel
    }

    companion object {
        val CHANNEL_NAME_GEN: String = "General"
        val CHANNEL_ID_GEN: String = "gen_001"
        val CHANNEL_UPDATE_STAT = "Status"
        val CHANNEL_ID_STAT = "stat_001"
    }
}