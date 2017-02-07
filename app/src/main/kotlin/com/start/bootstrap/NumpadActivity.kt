package com.start.bootstrap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.polidea.androidthings.driver.numpad.Numpad

class NumpadActivity : AppCompatActivity() {

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
                Log.d("detect keypad event", "${keyEvent.displayLabel}")
            }
        }.register()
    }
}
