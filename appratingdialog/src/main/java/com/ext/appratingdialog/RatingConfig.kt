package com.ext.appratingdialog

class RatingConfig {
    var daysBeforePrompt: Int = 3
    var launchTimes: Int = 5
    var minRatingToRedirect: Float = 4f
    var ratingListener: RatingListener? = null

    var titleText: String = "Rate our app"
    var submitText: String = "Submit"
    var laterText: String = "Remind me later"
    var neverText: String = "Never show again"

    var starColor: Int? = null
    var buttonBgColor: Int? = null
}