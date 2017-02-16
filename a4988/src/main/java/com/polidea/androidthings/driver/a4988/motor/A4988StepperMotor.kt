package com.polidea.androidthings.driver.a4988.motor

import com.polidea.androidthings.driver.a4988.driver.A4988
import com.polidea.androidthings.driver.a4988.driver.A4988Resolution
import com.polidea.androidthings.driver.steppermotor.Direction
import com.polidea.androidthings.driver.steppermotor.motor.StepperMotor

class A4988StepperMotor(private val stepsPerRevolution: Int,
                        stepGpioId: String,
                        dirGpioId: String?,
                        ms1GpioId: String?,
                        ms2GpioId: String?,
                        ms3GpioId: String?,
                        enGpioId: String?) : StepperMotor() {

    constructor(stepsPerRevolution: Int, stepGpioId: String) :
            this(stepsPerRevolution, stepGpioId, null, null, null, null, null)

    constructor(stepsPerRevolution: Int, stepGpioId: String, dirGpioId: String) :
            this(stepsPerRevolution, stepGpioId, dirGpioId, null, null, null, null)

    val a4988: A4988
    var enabled: Boolean
        set(value) {
            a4988.enabled = value
        }
        get() = a4988.enabled

    init {
        a4988 = A4988(stepGpioId, dirGpioId, ms1GpioId, ms2GpioId, ms3GpioId, enGpioId)
        stepperMotorDriver = a4988
        stepperMotorDriver.open()
    }

    private fun getResolution(resolutionId: Int)
            = A4988Resolution.getFromId(resolutionId)

    override fun getStepsFromDegrees(degrees: Double, resolutionId: Int)
            = (degrees / CIRCLE_DEGREES.toDouble() * getStepsPerRevolution(resolutionId).toDouble()).toInt()

    override fun getDegreesFromSteps(steps: Int, resolutionId: Int)
            = steps.toDouble() * CIRCLE_DEGREES.toDouble() / getStepsPerRevolution(resolutionId).toDouble()

    override fun getStepDurationMillisForRPM(rpm: Double, resolutionId: Int)
            = MINUTE_MILLIS.toDouble() / getStepsPerRevolution(resolutionId).toDouble() / rpm

    override fun getStepsPerRevolution(resolutionId: Int)
            = (stepsPerRevolution.toDouble() * getResolution(resolutionId).getStepMultiplier().toDouble()).toInt()

    override fun getExecutionDurationNanos(rpm: Double, resolutionId: Int, steps: Int)
            = (getStepDurationMillisForRPM(rpm, resolutionId) * steps.toDouble() * NANOS_IN_SECOND.toDouble()).toLong()

    override fun getMotorRunner(stepsToPerform: Int, direction: Direction, resolutionId: Int, executionDurationNanos: Long)
            = A4988MotorRunner(a4988, stepsToPerform, direction, getResolution(resolutionId), executionDurationNanos)
}