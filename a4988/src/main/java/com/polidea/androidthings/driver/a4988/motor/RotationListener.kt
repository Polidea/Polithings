package com.polidea.androidthings.driver.a4988.motor

interface RotationListener {
    fun onStarted() {
    }

    fun onFinishedSuccessfully() {
    }

    fun onFinishedWithError(degreesToMove: Double, movedDegrees: Double, exception: Exception) {
    }
}