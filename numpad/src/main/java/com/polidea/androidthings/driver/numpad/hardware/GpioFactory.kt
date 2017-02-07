package com.polidea.androidthings.driver.numpad.hardware

import com.google.android.things.pio.PeripheralManagerService


class GpioFactory {
    fun openGpio(name: String)
        = PeripheralManagerService().openGpio(name)
}

