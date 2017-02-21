package com.polidea.androidthings.driver.steppermotor.motor

import com.polidea.androidthings.driver.steppermotor.Direction
import com.polidea.androidthings.driver.steppermotor.driver.StepDuration
import com.polidea.androidthings.driver.steppermotor.driver.StepperMotorDriver
import com.polidea.androidthings.driver.steppermotor.listener.StepsListener

abstract class MotorRunner (val motorDriver: StepperMotorDriver,
                                                 val steps: Int,
                                                 val direction: Direction,
                                                 val executionDurationNanos: Long) : Runnable {

    var stepsListener: StepsListener? = null
    var stepDurationNanos: Long = 0
    var executionStartNanos: Long = 0

    override fun run() {
        stepsListener?.onStarted()
        motorDriver.direction = direction
        applyResolution()
        stepDurationNanos = executionDurationNanos / steps.toLong()
        executionStartNanos = System.nanoTime()
        for (step in 1..steps) {
            if (Thread.currentThread().isInterrupted) {
                finishWithError(step, InterruptedException())
                return
            }
            try {
                motorDriver.performStep(getStepDuration(step))
            } catch (e: InterruptedException) {
                finishWithError(step, e)
                return
            }
        }
        stepsListener?.onFinishedSuccessfully()
    }

    abstract protected fun applyResolution()

    private fun finishWithError(performedSteps: Int, exception: Exception)
            = stepsListener?.onFinishedWithError(steps, performedSteps, exception)

    private fun getStepDuration(stepNumber: Int): StepDuration {
        val durationNanos = getStepFinishTimestamp(stepNumber) - System.nanoTime()
        var millis = durationNanos / NANOS_IN_MILLISECOND
        var nanos = durationNanos - (millis * NANOS_IN_MILLISECOND)
        if (millis < 0 || millis == 0L && nanos < MIN_STEP_DURATION_NANOS) {
            millis = 0L
            nanos = MIN_STEP_DURATION_NANOS
        }
        return StepDuration(millis, nanos.toInt())
    }

    private fun getStepFinishTimestamp(stepNumber: Int): Long
            = executionStartNanos + stepNumber.toLong() * stepDurationNanos

    companion object {
        private val NANOS_IN_MILLISECOND = 1000000L
        private val MIN_STEP_DURATION_NANOS = 2000L
    }
}