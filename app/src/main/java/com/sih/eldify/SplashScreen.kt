package com.sih.eldify


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {

    val CHANNEL_ID = "My Channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val sharedPreferences = getSharedPreferences("BASIC_DETAILS", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder : Notification

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder = Notification.Builder(this)
                .setSmallIcon(R.drawable.eldify)
                .setContentTitle("Emergency Contacts")
                .setContentText("Contact 1: " + sharedPreferences.getString("EM_CONTACT_1", null) + " Contact 2:" + sharedPreferences.getString("EM_CONTACT_2", null))
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setChannelId(CHANNEL_ID)
                .build()

            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "new channel",
                    NotificationManager.IMPORTANCE_LOW
                )
            )

        }else{

            builder = Notification.Builder(this)
                .setSmallIcon(R.drawable.eldify)
                .setContentTitle("Emergency Contacts")
                .setContentText("Contact one of the following numbers in case of emergency: " + sharedPreferences.getString("EM_CONTACT_1", null) + ", " + sharedPreferences.getString("EM_CONTACT_2", null))
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .build()
        }

        builder.flags =
            builder.flags or (Notification.FLAG_ONGOING_EVENT or Notification.FLAG_NO_CLEAR)
        manager.notify(1, builder)


        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.

        var newIntent : Intent
        Handler().postDelayed({
            if(sharedPreferences.getString("first_launch", null) == "true"){
                newIntent = Intent(this, Onboarding::class.java)
            }else{
                editor.putString("first_launch", "true")
                editor.apply()
                newIntent = Intent(this, BasicDetails::class.java)
            }
            startActivity(newIntent)
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.

    }
}