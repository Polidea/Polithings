package com.polidea.androidthings.driver.a4988.gpio

import com.google.android.things.pio.PeripheralManagerService

open class GpioFactory {
    open fun openGpio(name: String)
            = PeripheralManagerService().openGpio(name)
}