package com.polidea.androidthings.driver.a4988.motor

import com.polidea.androidthings.driver.a4988.Direction
import com.polidea.androidthings.driver.a4988.driver.A4988
import com.polidea.androidthings.driver.a4988.driver.PulseDuration
import com.polidea.androidthings.driver.a4988.driver.Resolution

class A4988MotorRunner(val a4988: A4988, val steps: Int, val direction: Direction,
                       val resolution: Resolution, val executionDurationNanos: Long) : Runnable {

    var rotationListener: RotationListener? = null
    var stepDurationNanos: Long = 0
    var executionStartNanos: Long = 0

    override fun run() {
        rotationListener?.onStarted()
        a4988.direction = direction
        a4988.resolution = resolution
        stepDurationNanos = executionDurationNanos / steps.toLong()
        executionStartNanos = System.nanoTime()
        for (step in 1..steps) {
            if (Thread.currentThread().isInterrupted) {
                finishWithError(step, InterruptedException())
                return
            }
            try {
                a4988.performStep(getStepDuration(step))
            } catch (e: InterruptedException) {
                finishWithError(step, e)
                return
            }
        }
        rotationListener?.onFinishedSuccessfully()
    }

    private fun finishWithError(performedSteps: Int, exception: Exception)
            = rotationListener?.onFinishedWithError(steps, performedSteps, exception)

    private fun getStepDuration(stepNumber: Int): PulseDuration {
        val durationNanos = getStepFinishTimestamp(stepNumber) - System.nanoTime()
        var millis = durationNanos / NANOS_IN_MILLISECOND
        var nanos = durationNanos - (millis * NANOS_IN_MILLISECOND)
        if (millis < 0 || millis == 0L && nanos < MIN_STEP_DURATION_NANOS) {
            millis = 0L
            nanos = MIN_STEP_DURATION_NANOS
        }
        return PulseDuration(millis, nanos.toInt())
    }

    private fun getStepFinishTimestamp(stepNumber: Int): Long
            = executionStartNanos + stepNumber.toLong() * stepDurationNanos

    interface RotationListener {
        fun onStarted() {
        }

        fun onFinishedSuccessfully() {
        }

        fun onFinishedWithError(stepsToPerform: Int, performedSteps: Int, exception: Exception) {
        }
    }

    companion object {
        val NANOS_IN_MILLISECOND = 1000000L
        val MIN_STEP_DURATION_NANOS = 2000L
    }
}