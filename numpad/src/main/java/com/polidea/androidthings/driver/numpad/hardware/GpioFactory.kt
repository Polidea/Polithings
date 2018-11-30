package com.polidea.androidthings.driver.numpad.hardware

import com.google.android.things.pio.PeripheralManager

class GpioFactory {
    fun openGpio(name: String) = PeripheralManager.getInstance().openGpio(name)
}

