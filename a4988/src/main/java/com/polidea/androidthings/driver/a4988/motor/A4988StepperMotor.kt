package com.polidea.androidthings.driver.a4988.motor

import com.polidea.androidthings.driver.a4988.Direction
import com.polidea.androidthings.driver.a4988.awaiter.DefaultAwaiter
import com.polidea.androidthings.driver.a4988.driver.A4988
import com.polidea.androidthings.driver.a4988.driver.Resolution
import com.polidea.androidthings.driver.a4988.gpio.GpioFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class A4988StepperMotor internal constructor(private val stepsPerRevolution: Int,
                                             private val stepGpioId: String,
                                             private val dirGpioId: String?,
                                             private val ms1GpioId: String?,
                                             private val ms2GpioId: String?,
                                             private val ms3GpioId: String?,
                                             private val enGpioId: String?,
                                             private val gpioFactory: GpioFactory) : AutoCloseable {

    constructor(stepsPerRevolution: Int, stepGpioId: String) :
            this(stepsPerRevolution, stepGpioId, null, null, null, null, null, GpioFactory())

    constructor(stepsPerRevolution: Int, stepGpioId: String, dirGpioId: String) :
            this(stepsPerRevolution, stepGpioId, dirGpioId, null, null, null, null, GpioFactory())

    constructor(stepsPerRevolution: Int,
                stepGpioId: String,
                dirGpioId: String?,
                ms1GpioId: String?,
                ms2GpioId: String?,
                ms3GpioId: String?,
                enGpioId: String?) :
            this(stepsPerRevolution, stepGpioId, dirGpioId, ms1GpioId, ms2GpioId, ms3GpioId, enGpioId, GpioFactory())

    private val executor: ExecutorService
    private val a4988: A4988

    var enabled: Boolean
        set(value) {
            a4988.enabled = value
        }
        get() = a4988.enabled

    init {
        executor = Executors.newSingleThreadExecutor()
        a4988 = A4988(stepGpioId, dirGpioId, ms1GpioId, ms2GpioId, ms3GpioId, enGpioId, gpioFactory, DefaultAwaiter())
        a4988.open()
    }

    fun rotate(degrees: Double, direction: Direction, resolution: Resolution, rpm: Double)
            = rotate(degrees, direction, resolution, rpm, null)

    fun rotate(degrees: Double, direction: Direction, resolution: Resolution, rpm: Double, rotationListener: RotationListener?) {
        if (degrees < 0) {
            throw IllegalArgumentException("degrees less than 0: {$degrees}")
        }
        if (rpm < 0) {
            throw IllegalArgumentException("rpm less than 0: {$rpm}")
        }

        val stepsToPerform = getStepsFromDegrees(degrees, resolution)
        val executionDuration = getExecutionDurationNanos(rpm, resolution, stepsToPerform)
        val motorRunner = A4988MotorRunner(a4988, stepsToPerform, direction, resolution, executionDuration)
        motorRunner.rotationListener = object : A4988MotorRunner.RotationListener {
            override fun onStarted() {
                rotationListener?.onStarted()
            }

            override fun onFinishedSuccessfully() {
                rotationListener?.onFinishedSuccessfully()
            }

            override fun onFinishedWithError(stepsToPerform: Int, performedSteps: Int, exception: Exception) {
                rotationListener?.onFinishedWithError(degrees, getDegreesFromSteps(performedSteps, resolution), exception)
            }

        }

        executor.submit(motorRunner)
    }

    private fun getStepsFromDegrees(degrees: Double, resolution: Resolution)
            = (degrees / CIRCLE_DEGREES.toDouble() * getStepsPerRevolution(resolution)).toInt()

    private fun getDegreesFromSteps(steps: Int, resolution: Resolution)
            = steps.toDouble() * CIRCLE_DEGREES.toDouble() / getStepsPerRevolution(resolution)

    private fun getStepDurationMillisForRPM(rpm: Double, resolution: Resolution): Double
            = MINUTE_MILLIS.toDouble() / getStepsPerRevolution(resolution) / rpm

    private fun getStepsPerRevolution(resolution: Resolution)
            = stepsPerRevolution.toDouble() * resolution.getStepMultiplier().toDouble()

    private fun getExecutionDurationNanos(rpm: Double, resolution: Resolution, steps: Int): Long
            = (getStepDurationMillisForRPM(rpm, resolution) * steps.toDouble() * NANOS_IN_SECOND.toDouble()).toLong()

    override fun close() {
        executor.shutdownNow()
        a4988.close()
    }

    companion object {
        val CIRCLE_DEGREES = 360
        val MINUTE_MILLIS = 60 * 1000
        val NANOS_IN_SECOND = 1000000
    }
}