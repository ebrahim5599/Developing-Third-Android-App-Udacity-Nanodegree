package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private var fileName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            // Start Animation
            custom_button.buttonState = ButtonState.Clicked
            download()
        }
        createNotificationChannel()
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            // End Animation
            val downloadManager = getSystemService(DownloadManager::class.java)
            custom_button.buttonState = ButtonState.Completed
            //DownloadManager.Query() is used to filter DownloadManager queries
            val query = DownloadManager.Query()

            if (id != null) {
                query.setFilterById(downloadID)
            }

            val cursor = downloadManager.query(query)

            var notification = getSystemService(NotificationManager::class.java)
            if (cursor.moveToFirst()){

                when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                    DownloadManager.STATUS_SUCCESSFUL ->{

                        notification.sendMessage(this@MainActivity.applicationContext, CHANNEL_ID,"success",fileName)
                    }
                    DownloadManager.STATUS_FAILED -> {

                        notification.sendMessage(this@MainActivity.applicationContext, CHANNEL_ID,"failed",fileName)
                    }

                }}

        }
    }

    private fun download() {
        if (urlOption.isNotEmpty()) {
            custom_button.buttonState = ButtonState.Loading
            val request =
                DownloadManager.Request(Uri.parse(urlOption))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        }else
            Toast.makeText(this, getString(R.string.please_select_the_file), Toast.LENGTH_SHORT).show()
    }



    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private var urlOption: String = ""
        private const val CHANNEL_ID = "channelId"
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.option_item_one_glide ->
                    if (checked) {
                        // 1st option
//                        Toast.makeText(this, "1st option", Toast.LENGTH_SHORT).show()
                        fileName = getString(R.string.radio_button_option_one_glide)
                        urlOption = "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
                    }
                R.id.option_item_two_load_app ->
                    if (checked) {
                        // 2nd option
//                        Toast.makeText(this, "2nd option", Toast.LENGTH_SHORT).show()

                        fileName = getString(R.string.radio_button_option_two_load_app)
                        urlOption = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/refs/heads/master.zip"
                    }
                R.id.option_item_three_retrofit ->
                    if (checked) {
                        // 3rd option
//                        Toast.makeText(this, "3rd option", Toast.LENGTH_SHORT).show()

                        fileName = getString(R.string.radio_button_option_three_retrofit)
                        urlOption = "https://github.com/square/retrofit/archive/refs/heads/master.zip"
                    }
            }
        }
    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_title)
            val descriptionText = getString(R.string.notification_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}
