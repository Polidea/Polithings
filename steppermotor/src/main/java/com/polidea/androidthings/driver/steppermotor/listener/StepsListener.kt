package com.polidea.androidthings.driver.steppermotor.listener

interface StepsListener {
    fun onStarted() {
    }

    fun onFinishedSuccessfully() {
    }

    fun onFinishedWithError(stepsToPerform: Int, performedSteps: Int, exception: Exception) {
    }
}