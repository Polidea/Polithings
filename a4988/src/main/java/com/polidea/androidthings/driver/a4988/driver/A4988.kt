package com.polidea.androidthings.driver.a4988.driver

import android.util.Log
import com.polidea.androidthings.driver.steppermotor.Direction
import com.polidea.androidthings.driver.steppermotor.awaiter.Awaiter
import com.polidea.androidthings.driver.steppermotor.awaiter.DefaultAwaiter
import com.polidea.androidthings.driver.steppermotor.driver.StepDuration
import com.polidea.androidthings.driver.steppermotor.driver.StepperMotorDriver
import com.polidea.androidthings.driver.steppermotor.gpio.GpioFactory
import com.polidea.androidthings.driver.steppermotor.gpio.StepperMotorGpio
import java.io.IOException

class A4988 internal constructor(private val stepGpioId: String,
                                 private val dirGpioId: String?,
                                 private val ms1GpioId: String?,
                                 private val ms2GpioId: String?,
                                 private val ms3GpioId: String?,
                                 private val enGpioId: String?,
                                 private val gpioFactory: GpioFactory,
                                 private val awaiter: Awaiter) : StepperMotorDriver() {

    constructor(stepGpioId: String) :
            this(stepGpioId, null, null, null, null, null, GpioFactory(), DefaultAwaiter())

    constructor(stepGpioId: String,
                dirGpioId: String) :
            this(stepGpioId, dirGpioId, null, null, null, null, GpioFactory(), DefaultAwaiter())

    constructor(stepGpioId: String,
                dirGpioId: String?,
                ms1GpioId: String?,
                ms2GpioId: String?,
                ms3GpioId: String?,
                enGpioId: String?) :
            this(stepGpioId, dirGpioId, ms1GpioId, ms2GpioId, ms3GpioId, enGpioId, GpioFactory(), DefaultAwaiter())

    override var direction = Direction.CLOCKWISE
        set(value) {
            setDirectionOnBoard(value)
            field = value
        }

    var resolution = A4988Resolution.SIXTEENTH
        set(value) {
            setResolutionOnBoard(value)
            field = value
        }

    var enabled = false
        set(value) {
            setEnabledOnBoard(value)
            field = value
        }

    private lateinit var stepGpio: StepperMotorGpio
    private var dirGpio: StepperMotorGpio? = null
    private var ms1Gpio: StepperMotorGpio? = null
    private var ms2Gpio: StepperMotorGpio? = null
    private var ms3Gpio: StepperMotorGpio? = null
    private var enGpio: StepperMotorGpio? = null

    private var gpiosOpened = false

    override fun open() {
        if (gpiosOpened) {
            return
        }

        try {
            stepGpio = stepGpioId.openGpio()!!
            dirGpio = dirGpioId.openGpio()
            ms1Gpio = ms1GpioId.openGpio()
            ms2Gpio = ms2GpioId.openGpio()
            ms3Gpio = ms3GpioId.openGpio()
            enGpio = enGpioId.openGpio()
            gpiosOpened = true
        } catch (e: Exception) {
            close()
            throw e
        }

        direction = Direction.CLOCKWISE
        resolution = A4988Resolution.SIXTEENTH
        enabled = false
    }

    override fun close() {
        arrayOf(stepGpio, dirGpio, ms1Gpio, ms2Gpio, ms3Gpio, enGpio).forEach {
            try {
                it?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Couldn't close a gpio correctly.", e.cause)
            }
        }
        gpiosOpened = false
    }

    override fun performStep(stepDuration: StepDuration) {
        if (enGpio != null && !enabled) {
            throw IllegalStateException("A4988 is disabled. Enable it before performing a stepDuration.")
        }

        val pulseDurationMillis = stepDuration.millis / 2
        val pulseDurationNanos = stepDuration.nanos / 2

        stepGpio.value = true
        awaiter.await(pulseDurationMillis, pulseDurationNanos)
        stepGpio.value = false
        awaiter.await(pulseDurationMillis, pulseDurationNanos)
    }

    private fun setDirectionOnBoard(direction: Direction) {
        when (direction) {
            Direction.CLOCKWISE -> dirGpio?.value = true
            Direction.COUNTERCLOCKWISE -> dirGpio?.value = false
        }
    }

    private fun setResolutionOnBoard(a4988Resolution: A4988Resolution) {
        when (a4988Resolution) {
            A4988Resolution.FULL -> setResolutionGpios(true, true, true)
            A4988Resolution.HALF -> setResolutionGpios(true, true, false)
            A4988Resolution.QUARTER -> setResolutionGpios(false, true, false)
            A4988Resolution.EIGHT -> setResolutionGpios(true, false, false)
            A4988Resolution.SIXTEENTH -> setResolutionGpios(false, false, false)
        }
    }

    private fun setEnabledOnBoard(enabled: Boolean) {
        enGpio?.value = !enabled
    }

    private fun setResolutionGpios(ms1Value: Boolean, ms2Value: Boolean, ms3Value: Boolean) {
        ms1Gpio?.value = ms1Value
        ms2Gpio?.value = ms2Value
        ms3Gpio?.value = ms3Value
    }

    private fun String?.openGpio(): StepperMotorGpio?
            = if (this != null) StepperMotorGpio(gpioFactory.openGpio(this)) else null

    companion object {
        val TAG = "A4988"
    }
}