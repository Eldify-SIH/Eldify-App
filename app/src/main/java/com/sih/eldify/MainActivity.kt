package com.sih.eldify

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.sih.eldify.assistant.AssistantActivity
import com.sih.eldify.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.nav_header_main.view.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    // permission code
    val RECORDAUDIO : Int = 1
    private var REQUESTCALL = 2
    private var SENDSMS = 3

    private lateinit var music : MediaPlayer


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        // getting permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            checkPermissions()
        }



        binding.appBarMain.fab.setOnClickListener { view ->
            startActivity(Intent(this@MainActivity, AssistantActivity::class.java))
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val menuItem: MenuItem = navView.menu.findItem(R.id.app_bar_switch) // first insialize MenuItem

        val switchButton = menuItem.actionView.findViewById<View>(R.id.darkModeSwitch) as Switch
        switchButton.setOnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            if (b) {
                startmusic()
            } else {
                stopmusic()
            }
        }
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_settings, R.id.botFragment, R.id.sosFragment, R.id.videoFragment, R.id.nav_az
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        NavigationUI.setupWithNavController(binding.appBarMain.bottomNavigationView, navController)

        val sharedPreferences = getSharedPreferences("BASIC_DETAILS", Context.MODE_PRIVATE)
        val user_name = sharedPreferences.getString("USER_NAME", null)
        val user_email = sharedPreferences.getString("USER_EMAIL", null)

        val navHeaderView = navView.getHeaderView(0)

        if(user_name != null && user_email != null){
            navHeaderView.nav_user_name.text = user_name
            navHeaderView.nav_user_email.text = user_email
        }
    }

    private fun startmusic(){
        var music_list= arrayOf(
            R.raw.music1, R.raw.music2,
            R.raw.music3, R.raw.music4, R.raw.music5)
        val r= (0..4).random()
        music = MediaPlayer.create(this,music_list[r]);
        music.start()
    }

    private fun stopmusic(){
        music.stop()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // on request permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // for audio
        if (requestCode == 1 && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) Toast.makeText(
                this,
                "Permissions Granted",
                Toast.LENGTH_SHORT
            ).show()
            else Toast.makeText(
                this,
                "" +
                        "Permissions Denied",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CAMERA
            ),
            1
        )
    }
}