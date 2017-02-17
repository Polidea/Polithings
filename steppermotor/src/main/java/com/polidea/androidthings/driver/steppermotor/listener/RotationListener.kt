package com.polidea.androidthings.driver.steppermotor.listener

interface RotationListener {
    fun onStarted() {
    }

    fun onFinishedSuccessfully() {
    }

    fun onFinishedWithError(degreesToMove: Double, movedDegrees: Double, exception: Exception) {
    }
}