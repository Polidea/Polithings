package com.polidea.androidthings.driver.uln2003.driver

import com.polidea.androidthings.driver.steppermotor.Direction
import com.polidea.androidthings.driver.steppermotor.awaiter.Awaiter
import com.polidea.androidthings.driver.steppermotor.awaiter.DefaultAwaiter
import com.polidea.androidthings.driver.steppermotor.driver.StepDuration
import com.polidea.androidthings.driver.steppermotor.driver.StepperMotorDriver
import com.polidea.androidthings.driver.steppermotor.gpio.GpioFactory
import com.polidea.androidthings.driver.steppermotor.gpio.StepperMotorGpio

class ULN2003 internal constructor(private val in1GpioId: String,
                                   private val in2GpioId: String,
                                   private val in3GpioId: String,
                                   private val in4GpioId: String,
                                   private val gpioFactory: GpioFactory,
                                   private val awaiter: Awaiter) : StepperMotorDriver() {

    constructor(in1GpioId: String, in2GpioId: String, in3GpioId: String, in4GpioId: String) :
            this(in1GpioId, in2GpioId, in3GpioId, in4GpioId, GpioFactory(), DefaultAwaiter())

    var resolution = ULN2003Resolution.HALF

    private var in1: StepperMotorGpio? = null
    private var in2: StepperMotorGpio? = null
    private var in3: StepperMotorGpio? = null
    private var in4: StepperMotorGpio? = null

    private var currentStepState = INITIAL_STATE
    private var gpiosOpened = false

    override fun performStep(stepDuration: StepDuration) {
        when (resolution) {
            ULN2003Resolution.FULL -> setNextHalfStepState()
            ULN2003Resolution.HALF -> setNextFullStepState()
        }
        setActiveCoilsDependingOnCurrentStepState()
        awaiter.await(stepDuration.millis, stepDuration.nanos)
    }

    private fun setActiveCoilsDependingOnCurrentStepState() {
        when (currentStepState) {
            0 -> setActiveCoils(true, false, false, false)
            1 -> setActiveCoils(true, true, false, false)
            2 -> setActiveCoils(false, true, false, false)
            3 -> setActiveCoils(false, true, true, false)
            4 -> setActiveCoils(false, false, true, false)
            5 -> setActiveCoils(false, false, true, true)
            6 -> setActiveCoils(false, false, false, true)
            7 -> setActiveCoils(true, false, false, true)
        }
    }

    private fun setNextHalfStepState() {
        if (currentStepState == INITIAL_STATE) {
            currentStepState = 0
        } else {
            var nextStepState = currentStepState

            when (direction) {
                Direction.CLOCKWISE -> {
                    nextStepState++
                }
                Direction.COUNTERCLOCKWISE -> {
                    nextStepState--
                }
            }
            currentStepState = mapToAllowedState(nextStepState)
        }
    }

    private fun setNextFullStepState() {
        if (currentStepState == INITIAL_STATE) {
            currentStepState = 1
        } else {
            var nextStepState = currentStepState

            when (direction) {
                Direction.CLOCKWISE -> {
                    if (isCurrentStepFull()) {
                        nextStepState++
                    } else {
                        nextStepState += 2
                    }
                }
                Direction.COUNTERCLOCKWISE -> {
                    if (isCurrentStepFull()) {
                        nextStepState--
                    } else {
                        nextStepState -= 2
                    }
                }
            }
            currentStepState = mapToAllowedState(nextStepState)
        }
    }

    private fun isCurrentStepFull()
            = currentStepState % 2 == 0

    private fun mapToAllowedState(step: Int): Int {
        if (step >= STATE_STEPS_COUNT) {
            return step % STATE_STEPS_COUNT
        } else if (step < 0) {
            return STATE_STEPS_COUNT + step
        }

        return step
    }

    private fun setActiveCoils(in1State: Boolean, in2State: Boolean, in3State: Boolean, in4State: Boolean) {
        in1?.value = in1State
        in2?.value = in2State
        in3?.value = in3State
        in4?.value = in4State
    }

    override fun open() {
        if (gpiosOpened) {
            return
        }

        try {
            in1 = StepperMotorGpio(gpioFactory.openGpio(in1GpioId))
            in2 = StepperMotorGpio(gpioFactory.openGpio(in2GpioId))
            in3 = StepperMotorGpio(gpioFactory.openGpio(in3GpioId))
            in4 = StepperMotorGpio(gpioFactory.openGpio(in4GpioId))
            gpiosOpened = true
        } catch (e: Exception) {
            close()
            throw e
        }

        setActiveCoils(false, false, false, false)
    }

    override fun close() {
        arrayOf(in1, in2, in3, in4).forEach {
            try {
                it?.close()
            } catch (e: Exception) {
            }
        }
        gpiosOpened = false
    }

    companion object {
        private val STATE_STEPS_COUNT = 8
        private val INITIAL_STATE = -1
    }
}