package com.polidea.androidthings.driver.numpad

import android.util.Log
import android.view.InputDevice
import com.google.android.things.userdriver.InputDriver
import com.google.android.things.userdriver.UserDriverManager


class NumpadDriver(private val c1GpioId: String,
                   private val c2GpioId: String,
                   private val c3GpioId: String,
                   private val r1GpioId: String,
                   private val r2GpioId: String,
                   private val r3GpioId: String,
                   private val r4GpioId: String)
    : AutoCloseable {

    private val DRIVER_NAME = "Numpad12"
    private val DRIVER_VERSION = 1

    private val userDriverManager: UserDriverManager by lazy { UserDriverManager.getManager() }
    private val inputDriver: InputDriver by lazy { build(numpad) }
    private val numpad: Numpad by lazy {
            Numpad(c1GpioId, c2GpioId, c3GpioId,
                    r1GpioId, r2GpioId, r3GpioId, r4GpioId)
                    .apply { register() }
    }

    fun register() {
        userDriverManager.registerInputDriver(inputDriver)
    }

    fun unregister() {
        userDriverManager.unregisterInputDriver(inputDriver)
        numpad.unregister()
    }

    override fun close() {
        unregister()
    }

    private fun build(numpad: Numpad): InputDriver {
        val inputDriver = InputDriver.builder(InputDevice.SOURCE_KEYBOARD)
                .setName(DRIVER_NAME)
                .setVersion(DRIVER_VERSION)
                .setKeys(Numpad.Key.values().map { it.keyCode }.toIntArray())
                .build()
        numpad.keyListener = { keyEvent ->
            inputDriver.emit(arrayOf(keyEvent))
        }
        return inputDriver
    }
}