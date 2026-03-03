package com.ext.appratingdialog

interface RatingListener {
    fun onRateClicked(rating: Float)
    fun onLaterClicked()
}