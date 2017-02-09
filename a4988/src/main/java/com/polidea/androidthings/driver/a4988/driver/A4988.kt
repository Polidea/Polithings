package com.polidea.androidthings.driver.a4988.driver

import com.polidea.androidthings.driver.a4988.Direction
import com.polidea.androidthings.driver.a4988.gpio.A4988Gpio
import com.polidea.androidthings.driver.a4988.gpio.GpioFactory

class A4988 internal constructor(private val stepGpioId: String,
                                 private val dirGpioId: String?,
                                 private val ms1GpioId: String?,
                                 private val ms2GpioId: String?,
                                 private val ms3GpioId: String?,
                                 private val enGpioId: String?,
                                 private val gpioFactory: GpioFactory) : AutoCloseable {

    constructor(stepGpioId: String) :
            this(stepGpioId, null, null, null, null, null, GpioFactory())

    constructor(stepGpioId: String,
                dirGpioId: String) :
            this(stepGpioId, dirGpioId, null, null, null, null, GpioFactory())

    constructor(stepGpioId: String,
                dirGpioId: String?,
                ms1GpioId: String?,
                ms2GpioId: String?,
                ms3GpioId: String?,
                enGpioId: String?) :
            this(stepGpioId, dirGpioId, ms1GpioId, ms2GpioId, ms3GpioId, enGpioId, GpioFactory())

    var direction = Direction.CLOCKWISE
        set(value) {
            setDirectionOnBoard(value)
            field = value
        }

    var resolution = Resolution.SIXTEENTH
        set(value) {
            setResolutionOnBoard(value)
            field = value
        }

    var enabled = false
        set(value) {
            setEnabledOnBoard(value)
            field = value
        }

    private lateinit var stepGpio: A4988Gpio
    private var dirGpio: A4988Gpio? = null
    private var ms1Gpio: A4988Gpio? = null
    private var ms2Gpio: A4988Gpio? = null
    private var ms3Gpio: A4988Gpio? = null
    private var enGpio: A4988Gpio? = null

    private var gpiosOpened = false

    fun open() {
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
        resolution = Resolution.SIXTEENTH
        enabled = false
    }

    override fun close() {
        arrayOf(stepGpio, dirGpio, ms1Gpio, ms2Gpio, ms3Gpio).forEach {
            try {
                it?.close()
            } finally {
            }
        }
        gpiosOpened = false
    }

    fun performStep(stepPulseDuration: PulseDuration) {
        val pulseDurationMillis = stepPulseDuration.millis / 2
        val pulseDurationNanos = stepPulseDuration.nanos / 2

        stepGpio.value = true
        Thread.sleep(pulseDurationMillis, pulseDurationNanos)
        stepGpio.value = false
        Thread.sleep(pulseDurationMillis, pulseDurationNanos)
    }

    private fun setDirectionOnBoard(direction: Direction) {
        when (direction) {
            Direction.CLOCKWISE -> dirGpio?.value = true
            Direction.COUNTERCLOCKWISE -> dirGpio?.value = false
        }
    }

    private fun setResolutionOnBoard(resolution: Resolution) {
        when (resolution) {
            Resolution.FULL -> setResolutionGpios(true, true, true)
            Resolution.HALF -> setResolutionGpios(true, true, false)
            Resolution.QUARTER -> setResolutionGpios(false, true, false)
            Resolution.EIGHT -> setResolutionGpios(true, false, false)
            Resolution.SIXTEENTH -> setResolutionGpios(false, false, false)
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

    private fun String?.openGpio(): A4988Gpio?
            = if (this != null) A4988Gpio(gpioFactory.openGpio(this)) else null
}