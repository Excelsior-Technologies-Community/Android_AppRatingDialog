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

        // 1. Find Views
        val ratingBar = dialog.findViewById<RatingBar>(R.id.ratingBar)
        val btnSubmit = dialog.findViewById<Button>(R.id.btnSubmit)
        val tvLater = dialog.findViewById<TextView>(R.id.tvLater)
        val tvNever = dialog.findViewById<TextView>(R.id.tvNever)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)

        // 2. Apply Custom Text
        tvTitle.text = config.titleText
        btnSubmit.text = config.submitText
        tvLater.text = config.laterText
        tvNever.text = config.neverText

        // 3. Apply Button Color
        config.buttonBgColor?.let { color ->
            btnSubmit.backgroundTintList = android.content.res.ColorStateList.valueOf(color)
        }

        // 4. ⭐ Apply Star Color ONLY when user selects rating
        ratingBar.setOnRatingBarChangeListener { _, _, fromUser ->
            if (fromUser) {
                config.starColor?.let { color ->
                    val drawable = ratingBar.progressDrawable as android.graphics.drawable.LayerDrawable

                    val progressDrawable = drawable.getDrawable(2) // ⭐ filled stars
                    val wrapped = androidx.core.graphics.drawable.DrawableCompat.wrap(progressDrawable)
                    androidx.core.graphics.drawable.DrawableCompat.setTint(wrapped, color)

                    drawable.setDrawableByLayerId(android.R.id.progress, wrapped)
                }
            }
        }

        val prefs = RatingPreferences(context)

        // 5. Click Listeners

        btnSubmit.setOnClickListener {
            val rating = ratingBar.rating

            config.ratingListener?.onRateClicked(rating)

            if (rating >= config.minRatingToRedirect) {
                prefs.setNeverShowAgain()
                openPlayStore()
            }

            dialog.dismiss()
        }

        tvLater.setOnClickListener {
            config.ratingListener?.onLaterClicked()
            dialog.dismiss()
        }

        tvNever.setOnClickListener {
            prefs.setNeverShowAgain()
            dialog.dismiss()
        }

        // 6. Show Dialog
        dialog.show()
    }

    private fun shouldShowDialog(): Boolean {
        val prefs = RatingPreferences(context)
        if (prefs.isNeverShowAgain()) return false
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

        fun setTitleText(text: String) = apply {
            config.titleText = text
        }

        fun setSubmitText(text: String) = apply {
            config.submitText = text
        }

        fun setLaterText(text: String) = apply {
            config.laterText = text
        }

        fun setNeverText(text: String) = apply {
            config.neverText = text
        }

        fun setStarColor(color: Int) = apply {
            config.starColor = color
        }

        fun setButtonBackgroundColor(color: Int) = apply {
            config.buttonBgColor = color
        }
    }
}