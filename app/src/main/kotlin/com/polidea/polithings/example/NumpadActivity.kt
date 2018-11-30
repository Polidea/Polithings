package com.polidea.polithings.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import com.polidea.androidthings.driver.numpad.Numpad

class NumpadActivity : AppCompatActivity() {

    private val TAG = "NumpadActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Numpad(c1GpioId = "BCM27",
                c2GpioId = "BCM17",
                c3GpioId = "BCM22",
                r1GpioId = "BCM12",
                r2GpioId = "BCM21",
                r3GpioId = "BCM20",
                r4GpioId = "BCM16"
        )
                .apply {
                    keyListener = { keyEvent ->
                        val actionName = when (keyEvent.action) {
                            KeyEvent.ACTION_DOWN -> "ACTION_DOWN"
                            KeyEvent.ACTION_UP -> "ACTION_UP"
                            else -> keyEvent.action.toString()
                        }
                        Log.d(TAG, "Detected keypad key ${keyEvent.displayLabel} with action $actionName")
                    }
                }.register()
    }
}