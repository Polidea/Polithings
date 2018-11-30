package com.polidea.androidthings.driver.numpad

import android.view.KeyEvent
import com.google.android.things.userdriver.UserDriverManager
import com.google.android.things.userdriver.input.InputDriver
import com.google.android.things.userdriver.input.InputDriverEvent


class NumpadDriver(private val c1GpioId: String,
                   private val c2GpioId: String,
                   private val c3GpioId: String,
                   private val r1GpioId: String,
                   private val r2GpioId: String,
                   private val r3GpioId: String,
                   private val r4GpioId: String)
    : AutoCloseable {

    private val DRIVER_NAME = "Numpad12"

    private val userDriverManager: UserDriverManager by lazy { UserDriverManager.getInstance() }
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
        val inputDriver = InputDriver.Builder()
                .setName(DRIVER_NAME)
                .setSupportedKeys(Numpad.Key.values().map { it.keyCode }.toIntArray())
                .build()
        numpad.keyListener = { keyEvent ->
            val event = InputDriverEvent()
            event.setKeyPressed(keyEvent.keyCode, keyEvent.action == KeyEvent.ACTION_DOWN)
            inputDriver.emit(event)
        }
        return inputDriver
    }
}