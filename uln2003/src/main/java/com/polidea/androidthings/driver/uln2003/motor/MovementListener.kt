package com.polidea.androidthings.driver.uln2003.motor

interface MovementListener {
    fun onStarted() {
    }

    fun onFinishedSuccessfully() {
    }

    fun onFinishedWithError(angleToMove: Double, movedAngle: Double, exception: Exception) {
    }
}