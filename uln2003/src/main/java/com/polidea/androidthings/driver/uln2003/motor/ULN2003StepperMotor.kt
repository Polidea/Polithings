package com.polidea.androidthings.driver.uln2003.motor

import com.polidea.androidthings.driver.uln2003.*
import com.polidea.androidthings.driver.uln2003.driver.ULN2003
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ULN2003StepperMotor(in1GpioId: String, in2GpioId: String, in3GpioId: String, in4GpioId: String) : AutoCloseable {

    private var uln2003: ULN2003
    private var executor: ExecutorService

    init {
        executor = Executors.newSingleThreadExecutor()
        uln2003 = ULN2003(in1GpioId, in2GpioId, in3GpioId, in4GpioId)
        uln2003.open()
    }

    fun move(angle: Double, direction: Direction, resolution: Resolution, stepInterval: Interval = Interval()) {
        move(angle, direction, resolution, null, stepInterval)
    }

    fun move(angle: Double, direction: Direction, resolution: Resolution, movementListener: MovementListener? = null, stepInterval: Interval = Interval()) {
        if (angle < 0) {
            throw IllegalArgumentException("angle less than 0: {$angle}")
        }

        val stepsPerRevolution = getStepsPerRevolution(resolution)
        val stepsToPerform = getStepsForAngle(angle, stepsPerRevolution)
        val motorRunner = ULN2003MotorRunner(uln2003, stepsToPerform, direction, resolution, stepInterval)
        motorRunner.movementListener = object : ULN2003MotorRunner.MovementListener {
            override fun onStarted() {
                movementListener?.onStarted()
            }

            override fun onFinishedSuccessfully() {
                movementListener?.onFinishedSuccessfully()
            }

            override fun onFinishedWithError(stepsToPerform: Int, performedSteps: Int, exception: Exception) {
                movementListener?.onFinishedWithError(angle, getAngleFromSteps(performedSteps, stepsPerRevolution), exception)
            }
        }
        executor.submit(motorRunner)
    }

    override fun close() {
        executor.shutdownNow()
        uln2003.close()
    }

    private fun getStepsPerRevolution(resolution: Resolution)
            = if (resolution == Resolution.FULL) HALF_STEPS_PER_REVOLUTION else FULL_STEPS_PER_REVOLUTION

    private fun getStepsForAngle(angle: Double, stepsPerRevolution: Int)
            = (angle / CIRCLE_DEGREES.toDouble() * stepsPerRevolution).toInt()

    private fun getAngleFromSteps(steps: Int, stepsPerRevolution: Int)
            = steps.toDouble() * CIRCLE_DEGREES.toDouble() / stepsPerRevolution

    companion object {
        /**
         * @see <a href="http://42bots.com/tutorials/28byj-48-stepper-motor-with-uln2003-driver-and-arduino-uno/">28BYJ-48 Stepper Motor tutorial</a>
         */
        val FULL_STEPS_PER_REVOLUTION = 4076
        val HALF_STEPS_PER_REVOLUTION = FULL_STEPS_PER_REVOLUTION / 2
        val CIRCLE_DEGREES = 360
    }
}