package com.polidea.androidthings.driver.numpad.hardware

import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManagerService
import java.io.IOException


class NumpadKey : AutoCloseable {

    enum class LogicState(val activeType: Int) {
        PRESSED_WHEN_HIGH(Gpio.ACTIVE_HIGH),
        PRESSED_WHEN_LOW(Gpio.ACTIVE_LOW)
    }

    private var mButtonGpio: Gpio? = null

    val state: Boolean
        get() = mButtonGpio!!.value

    constructor(pin: String, logicLevel: LogicState) {
        val pioService = PeripheralManagerService()
        val buttonGpio = pioService.openGpio(pin)
        try {
            connect(buttonGpio, logicLevel)
        } catch (e: IOException) {
            close()
            throw e
        } catch (e: RuntimeException) {
            close()
            throw e
        }
    }

    private fun connect(buttonGpio: Gpio, logicLevel: LogicState) {
        mButtonGpio = buttonGpio
        mButtonGpio?.setDirection(Gpio.DIRECTION_IN)
        mButtonGpio?.setEdgeTriggerType(Gpio.EDGE_BOTH)
        mButtonGpio?.setActiveType(logicLevel.activeType)
    }

    override fun close() {
        try {
            mButtonGpio?.close()
        } finally {
            mButtonGpio = null
        }
    }

}
