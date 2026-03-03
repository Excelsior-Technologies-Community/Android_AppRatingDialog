package com.ext.appratingdialog

import android.content.Context

class RatingPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("rating_prefs", Context.MODE_PRIVATE)

    fun incrementLaunchCount() {
        val count = getLaunchCount() + 1
        prefs.edit().putInt("launch_count", count).apply()
    }

    fun getLaunchCount(): Int {
        return prefs.getInt("launch_count", 0)
    }

    fun getDaysSinceFirstLaunch(): Int {
        val first = prefs.getLong("first_launch", 0L)

        if (first == 0L) {
            prefs.edit().putLong("first_launch", System.currentTimeMillis()).apply()
            return 0
        }

        val diff = System.currentTimeMillis() - first
        return (diff / (1000 * 60 * 60 * 24)).toInt()
    }
}