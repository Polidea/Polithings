package com.polidea.androidthings.driver.numpad.domain

import com.google.android.things.pio.Gpio
import com.polidea.androidthings.driver.numpad.hardware.GpioFactory


internal open class Column(gpioId: String,
                           open val id: Id,
                           gpioFactory: GpioFactory) : AutoCloseable {

    val gpio: Gpio = gpioFactory.openGpio(gpioId).apply {
        setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        value = true
    }

    open var state: Boolean = false
        set(value) { gpio.value = !value}

    override fun close() {
        gpio.close()
    }

    enum class Id { C1, C2, C3 }
}

internal open class ColumnFactory(val gpioFactory: GpioFactory = GpioFactory()) {

    open fun create(gpioId: String, id: Column.Id)
        = Column(gpioId, id, gpioFactory)
}

