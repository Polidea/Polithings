package com.polidea.androidthings.driver.uln2003.motor

import com.polidea.androidthings.driver.uln2003.Direction
import com.polidea.androidthings.driver.uln2003.motor.Interval
import com.polidea.androidthings.driver.uln2003.motor.Resolution
import com.polidea.androidthings.driver.uln2003.driver.ULN2003

class ULN2003MotorRunner(val uln2003: ULN2003, val steps: Int, val direction: Direction,
                         val resolution: Resolution, val stepInterval: Interval) : Runnable {

    var movementListener: MovementListener? = null

    override fun run() {
        movementListener?.onStarted()
        uln2003.direction = direction
        for (step in 0..steps) {
            if (resolution == Resolution.FULL) {
                uln2003.moveToNextHalfStep()
            } else {
                uln2003.moveToNextFullStep()
            }

            try {
                Thread.sleep(stepInterval.millis, stepInterval.nanos)
            } catch (e: InterruptedException) {
                movementListener?.onFinishedWithError(steps, step, e)
                return
            }
        }
        movementListener?.onFinishedSuccessfully()
    }

    interface MovementListener {
        fun onStarted()
        fun onFinishedSuccessfully()
        fun onFinishedWithError(stepsToPerform: Int, performedSteps: Int, exception: Exception)
    }
}