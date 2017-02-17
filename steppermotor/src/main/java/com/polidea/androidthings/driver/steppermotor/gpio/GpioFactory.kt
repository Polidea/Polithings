package com.polidea.androidthings.driver.steppermotor.gpio

import com.google.android.things.pio.PeripheralManagerService

open class GpioFactory {
    open fun openGpio(name: String)
            = PeripheralManagerService().openGpio(name)
}