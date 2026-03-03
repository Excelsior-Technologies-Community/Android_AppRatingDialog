package com.ext.appratingdialog

class RatingConfig {
    var daysBeforePrompt: Int = 3
    var launchTimes: Int = 5
    var minRatingToRedirect: Float = 4f
    var ratingListener: RatingListener? = null
}