package com.ext.appratingdialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView

class AppRatingDialog private constructor(
    private val context: Context,
    private val config: RatingConfig
) {

    fun show() {
        if (!shouldShowDialog()) return

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_rating)

        val ratingBar = dialog.findViewById<RatingBar>(R.id.ratingBar)
        val btnSubmit = dialog.findViewById<Button>(R.id.btnSubmit)
        val tvLater = dialog.findViewById<TextView>(R.id.tvLater)

        btnSubmit.setOnClickListener {
            val rating = ratingBar.rating
            config.ratingListener?.onRateClicked(rating)
            if (rating >= config.minRatingToRedirect) {
                openPlayStore()
            }

            dialog.dismiss()
        }
        tvLater.setOnClickListener {
            // ✅ CALLBACK
            config.ratingListener?.onLaterClicked()

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun shouldShowDialog(): Boolean {
        val prefs = RatingPreferences(context)

        return prefs.getLaunchCount() >= config.launchTimes &&
                prefs.getDaysSinceFirstLaunch() >= config.daysBeforePrompt
    }

    private fun openPlayStore() {
        val uri = Uri.parse("market://details?id=${context.packageName}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }

    class Builder(private val context: Context) {

        private val config = RatingConfig()

        fun setDaysBeforePrompt(days: Int) = apply {
            config.daysBeforePrompt = days
        }

        fun setLaunchTimes(times: Int) = apply {
            config.launchTimes = times
        }

        fun build(): AppRatingDialog {
            return AppRatingDialog(context, config)
        }
        fun setRatingListener(listener: RatingListener) = apply {
            config.ratingListener = listener
        }
    }
}