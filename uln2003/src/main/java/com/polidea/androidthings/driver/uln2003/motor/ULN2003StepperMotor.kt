package com.polidea.androidthings.driver.uln2003.motor

import com.polidea.androidthings.driver.steppermotor.Direction
import com.polidea.androidthings.driver.steppermotor.motor.StepperMotor
import com.polidea.androidthings.driver.uln2003.driver.ULN2003
import com.polidea.androidthings.driver.uln2003.driver.ULN2003Resolution

class ULN2003StepperMotor(in1GpioId: String,
                          in2GpioId: String,
                          in3GpioId: String,
                          in4GpioId: String) : StepperMotor() {

    val uln2003: ULN2003

    init {
        uln2003 = ULN2003(in1GpioId, in2GpioId, in3GpioId, in4GpioId)
        stepperMotorDriver = uln2003
        stepperMotorDriver.open()
    }

    private fun getResolution(resolutionId: Int)
            = ULN2003Resolution.getFromId(resolutionId)

    override fun getStepsFromDegrees(degrees: Double, resolutionId: Int)
            = (degrees / CIRCLE_DEGREES.toDouble() * getStepsPerRevolution(resolutionId).toDouble()).toInt()

    override fun getDegreesFromSteps(steps: Int, resolutionId: Int)
            = steps.toDouble() * CIRCLE_DEGREES.toDouble() / getStepsPerRevolution(resolutionId).toDouble()

    override fun getStepDurationMillisForRPM(rpm: Double, resolutionId: Int)
            = MINUTE_MILLIS.toDouble() / getStepsPerRevolution(resolutionId).toDouble() / rpm

    override fun getStepsPerRevolution(resolutionId: Int)
            = if (getResolution(resolutionId) == ULN2003Resolution.FULL) HALF_STEPS_PER_REVOLUTION else FULL_STEPS_PER_REVOLUTION

    override fun getExecutionDurationNanos(rpm: Double, resolutionId: Int, steps: Int)
            = (getStepDurationMillisForRPM(rpm, resolutionId) * steps.toDouble() * NANOS_IN_SECOND.toDouble()).toLong()

    override fun getMotorRunner(stepsToPerform: Int, direction: Direction, resolutionId: Int, executionDurationNanos: Long)
            = ULN2003MotorRunner(uln2003, stepsToPerform, direction, getResolution(resolutionId), executionDurationNanos)

    companion object {
        /**
         * @see <a href="http://42bots.com/tutorials/28byj-48-stepper-motor-with-uln2003-driver-and-arduino-uno/">28BYJ-48 Stepper Motor tutorial</a>
         */
        val HALF_STEPS_PER_REVOLUTION = 4076
        val FULL_STEPS_PER_REVOLUTION = HALF_STEPS_PER_REVOLUTION / 2
    }
}