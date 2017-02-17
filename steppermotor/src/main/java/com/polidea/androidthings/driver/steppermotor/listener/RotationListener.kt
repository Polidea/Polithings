package com.polidea.androidthings.driver.steppermotor.listener

interface RotationListener {
    fun onStarted() {
    }

    fun onFinishedSuccessfully() {
    }

    fun onFinishedWithError(degreesToRotate: Double, rotatedDegrees: Double, exception: Exception) {
    }
}