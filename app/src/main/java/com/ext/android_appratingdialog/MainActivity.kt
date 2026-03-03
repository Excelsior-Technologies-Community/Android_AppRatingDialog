package com.ext.android_appratingdialog

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ext.appratingdialog.AppRatingDialog
import com.ext.appratingdialog.RatingListener
import com.ext.appratingdialog.RatingPreferences

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = RatingPreferences(this)
        prefs.incrementLaunchCount()
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.decorView.post {
            AppRatingDialog.Builder(this)
                .setDaysBeforePrompt(0)
                .setLaunchTimes(1)
                .setRatingListener(object : RatingListener {
                    override fun onRateClicked(rating: Float) {
                        Log.d("Rating", "User rated: $rating")
                    }

                    override fun onLaterClicked() {
                        Log.d("Rating", "User clicked later")
                    }
                })
                .build()
                .show()
        }
    }
}