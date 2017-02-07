package com.polidea.androidthings.driver.uln2003.driver

import com.polidea.androidthings.driver.uln2003.Direction
import com.polidea.androidthings.driver.uln2003.gpio.GpioFactory
import com.polidea.androidthings.driver.uln2003.gpio.ULN2003Gpio

class ULN2003 internal constructor(private val in1GpioId: String,
                                   private val in2GpioId: String,
                                   private val in3GpioId: String,
                                   private val in4GpioId: String,
                                   private val gpioFactory: GpioFactory) : AutoCloseable {

    constructor(in1GpioId: String,
                in2GpioId: String,
                in3GpioId: String,
                in4GpioId: String) :
            this(in1GpioId, in2GpioId, in3GpioId, in4GpioId, GpioFactory())

    var direction: Direction = Direction.CLOCKWISE

    private var in1: ULN2003Gpio? = null
    private var in2: ULN2003Gpio? = null
    private var in3: ULN2003Gpio? = null
    private var in4: ULN2003Gpio? = null

    private var currentStepState = INITIAL_STATE
    private var gpiosOpened = false

    fun moveToNextHalfStep() {
        setNextHalfStepState()
        setActiveCoilsDependingOnCurrentStepState()
    }

    fun moveToNextFullStep() {
        setNextFullStepState()
        setActiveCoilsDependingOnCurrentStepState()
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

    fun open() {
        if (gpiosOpened) {
            return
        }

        try {
            in1 = ULN2003Gpio(gpioFactory.openGpio(in1GpioId))
            in2 = ULN2003Gpio(gpioFactory.openGpio(in2GpioId))
            in3 = ULN2003Gpio(gpioFactory.openGpio(in3GpioId))
            in4 = ULN2003Gpio(gpioFactory.openGpio(in4GpioId))
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
            } finally {
            }
        }
        gpiosOpened = false
    }

    companion object {
        private val STATE_STEPS_COUNT = 8
        private val INITIAL_STATE = -1
    }
}