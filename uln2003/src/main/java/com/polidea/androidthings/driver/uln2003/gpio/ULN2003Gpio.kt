package com.polidea.androidthings.driver.uln2003.gpio

import com.google.android.things.pio.Gpio

class ULN2003Gpio(val gpio: Gpio) : AutoCloseable {

    init {
        gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        gpio.setActiveType(Gpio.ACTIVE_HIGH)
    }

    var value: Boolean
        set(value) {
            gpio.value = value
        }
        get()
            = gpio.value

    override fun close()
            = gpio.close()
}